package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioDocumentoService;
import com.maticolque.apirestelevadores.service.MedioHabilitacionService;
import com.maticolque.apirestelevadores.service.RevisorService;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/v1/medioDocumento")
public class MedioDocumentoController {

    @Autowired
    private MedioDocumentoService medioDocumentoService;

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;

    @Autowired
    private RevisorService revisorService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Medio-Documento
            List<MedioDocumento> mediosDocumentos = medioDocumentoService.getAllMedioDocumento();

            // Verificar la lista de Medio-Documento
            if (mediosDocumentos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medio-Documento.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<MedioDocumentoReadDTO> medioDocumentoDTO = new ArrayList<>();
            for (MedioDocumento medioDocumento : mediosDocumentos) {
                MedioDocumentoReadDTO dto = MedioDocumentoReadDTO.fromEntity(medioDocumento);
                medioDocumentoDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioDocumento", medioDocumentoDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medio-Documento: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioDocumentoPorId(@PathVariable Integer id) {
        try {

            // Buscar MedioDocumento por ID
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            MedioDocumentoReadDTO medioDocumentoReadDTO = MedioDocumentoReadDTO.fromEntity(medioDocumentoExistente);

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioDocumento", medioDocumentoReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Medio Documento. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearMedioDocumento(@RequestBody MedioDocumentoCreateDTO createDTO) {
        try {
            // Validar datos
            if (createDTO.getMdo_adjunto_orden() == 0 || createDTO.getMdo_adjunto_fecha().isEmpty()) {
                throw new IllegalArgumentException("No se permiten datos vacíos.");
            }

            // Buscar Medio Habilitacion, Tipo Adjunto y Revisor por sus IDs
            MedioHabilitacion medioHabilitacion = medioHabilitacionService.buscarmedioHabilitacionPorId(createDTO.getMdo_mha_id());
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(createDTO.getMdo_tad_id());
            Revisor revisor = revisorService.buscarRevisorPorId(createDTO.getMdo_rev_id());

            // Verificar si los IDs existen
            if (medioHabilitacion == null) {
                throw new IllegalArgumentException("ID del Medio-Habilitacion no encontrado.");
            }
            if (tipoAdjunto == null) {
                throw new IllegalArgumentException("ID del TipoAdjunto no encontrado.");
            }
            if (revisor == null) {
                throw new IllegalArgumentException("ID del Revisor no encontrado.");
            }

            // Validar permisos rev_aprob_mde y rev_renov_mde
            if (!revisor.isRev_aprob_mde() && !revisor.isRev_renov_mde()) {
                throw new IllegalArgumentException("El revisor debe tener al menos uno de los campos rev_aprob_mde o rev_renov_mde en true.");
            }

            // Convertir DTO a entidad
            MedioDocumento medioDocumento = MedioDocumentoCreateDTO.toEntity(createDTO, medioHabilitacion, tipoAdjunto, revisor);

            // Crear Empresa-Habilitacion
            MedioDocumento nuevoMedioDocumento = medioDocumentoService.createMedioDocumento(medioDocumento);

            // Convertir entidad a DTO
            MedioDocumentoReadDTO nuevoMedioDocumentoReadDTO = MedioDocumentoReadDTO.fromEntity(nuevoMedioDocumento);

            // Mandar respuesta
            RespuestaDTO<MedioDocumentoReadDTO> respuesta = new RespuestaDTO<>(nuevoMedioDocumentoReadDTO, "Medio-Documento", "Medio-Documento creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Medio-Documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Medio-Documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarMedioDocumento(@PathVariable Integer id, @RequestBody MedioDocumentoUpdateDTO updateDTO) {
        try {

            // Obtener ID del Medio-Documento
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            // Verificar si existe el ID del Medio-Documento
            if (medioDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Medio-Documento con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (updateDTO.getMdo_adjunto_orden() == 0 || updateDTO.getMdo_adjunto_fecha().isEmpty()){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("No se permiten datos vacíos.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Obtener ID del nuevo TipoAdjunto y Revisor
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(updateDTO.getMdo_tad_id());
            Revisor revisor = revisorService.buscarRevisorPorId(updateDTO.getMdo_rev_id());

            // Verificar si los IDs existen
            if (tipoAdjunto == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el TipoAdjunto con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }
            if (revisor == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Revisor con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar permisos rev_aprob_mde y rev_renov_mde
            if (!revisor.isRev_aprob_mde() && !revisor.isRev_renov_mde()) {
                throw new IllegalArgumentException("El revisor debe tener al menos uno de los campos rev_aprob_mde o rev_renov_mde en true.");
            }

            // Convertir Strings a LocalDate para las fechas en el DTO
            LocalDate fecha = LocalDate.parse(updateDTO.getMdo_adjunto_fecha());

            // Actualizar los campos del Medio-Documento
            medioDocumentoExistente.setTipoAdjunto(tipoAdjunto);
            medioDocumentoExistente.setMdo_adjunto_orden(updateDTO.getMdo_adjunto_orden());
            medioDocumentoExistente.setMdo_adjunto_fecha(fecha);
            medioDocumentoExistente.setRevisor(revisor);

            //Actualizar Medio-Documento
            medioDocumentoService.updateMedioDocumento(medioDocumentoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar el Medio-Documento: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarMedioDocumento(@PathVariable Integer id) {
        try {
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                medioDocumentoService.deleteMedioDocumentoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Medio Documento eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Medio Documento. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}