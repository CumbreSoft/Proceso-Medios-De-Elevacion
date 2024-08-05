package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaHabilitacionService;
import com.maticolque.apirestelevadores.service.HabilitacionDocumentoService;
import com.maticolque.apirestelevadores.service.RevisorService;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/habilitacionDocumento")
public class HabilitacionDocumentoController {

    @Autowired
    private HabilitacionDocumentoService habilitacionDocumentoService;

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;

    @Autowired
    private RevisorService revisorService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Habilitacion-Documento
            List<HabilitacionDocumento> habilitacionDocumentos = habilitacionDocumentoService.getAllHabilitacionDocumento();

            // Verificar la lista de Habilitacion-Documento
            if (habilitacionDocumentos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Habilitacion-Documento.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<HabilitacionDocumentoReadDTO> habilitacionDocumentoDTO = new ArrayList<>();
            for (HabilitacionDocumento habilitacionDocumento : habilitacionDocumentos) {
                HabilitacionDocumentoReadDTO dto = HabilitacionDocumentoReadDTO.fromEntity(habilitacionDocumento);
                habilitacionDocumentoDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionDocumento", habilitacionDocumentoDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de de Habilitacion-Documento: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarHabilitacionDocumentoPorId(@PathVariable Integer id) {
        try {

            // Buscar HabilitacionDocumento por ID
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            // Verificar si existe el ID
            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            }

            // Convertir la entidad en un DTO
            HabilitacionDocumentoReadDTO habilitacionDocumentoReadDTO = HabilitacionDocumentoReadDTO.fromEntity(habilitacionDocumentoExistente);

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionDocumento", habilitacionDocumentoReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Habilitacion de Documentos. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearHabilitacionDocumento(@RequestBody HabilitacionDocumentoCreateDTO createDTO) {
        try {

            // Validar datos
            if (createDTO.getHdo_adjunto_orden() == 0 || createDTO.getHdo_adjunto_fecha().isEmpty()) {
                throw new IllegalArgumentException("No se permiten datos vacíos.");
            }

            // Buscar Empresa Habilitacion, Tipo Adjunto y Revisor por sus IDs
            EmpresaHabilitacion empresaHabilitacion = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(createDTO.getHdo_eha_id());
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(createDTO.getHdo_tad_id());
            Revisor revisor = revisorService.buscarRevisorPorId(createDTO.getHdo_rev_id());

            // Verificar si los IDs existen
            if (empresaHabilitacion == null) {
                throw new IllegalArgumentException("ID de Empresa-Habilitacion no encontrado.");
            }
            if(tipoAdjunto == null){
                throw new IllegalArgumentException("ID del TipoAdjunto no encontrado.");
            }
            if(revisor == null){
                throw new IllegalArgumentException("ID del Revisor no encontrado.");
            }

            // Validar permiso isRev_aprob_emp
            if (!revisor.isRev_aprob_emp()) {
                throw new IllegalArgumentException("El revisor debe tener el campo isRev_aprob_emp en true.");
            }

            // Convertir DTO a entidad
            HabilitacionDocumento habilitacionDocumento = HabilitacionDocumentoCreateDTO.toEntity(createDTO, empresaHabilitacion, tipoAdjunto, revisor);

            // Crear Habilitacion-Documento
            HabilitacionDocumento nuevaHabilitacionDocumento = habilitacionDocumentoService.createHabilitacionDocumento(habilitacionDocumento);

            // Convertir entidad a DTO
            HabilitacionDocumentoReadDTO nuevaHabilitacionDocumentoReadDTO = HabilitacionDocumentoReadDTO.fromEntity(nuevaHabilitacionDocumento);

            // Mandar respuesta
            RespuestaDTO<HabilitacionDocumentoReadDTO> respuesta = new RespuestaDTO<>(nuevaHabilitacionDocumentoReadDTO, "Habilitacion-Documento", "Habilitacion-Documento creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Habilitacion-Documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Habilitacion-Documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarHabilitacionDocumento(@PathVariable Integer id, @RequestBody HabilitacionDocumentoUpdateDTO updateDTO) {
        try {

            // Obtener ID de Habilitacion-Documento
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            // Verificar si existe el ID de Habilitacion-Documento
            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Habilitacion-Documento con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar los datos del DTO
            if (updateDTO.getHdo_adjunto_orden() == 0 || updateDTO.getHdo_adjunto_fecha().isEmpty()){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("No se permiten datos vacíos.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Obtener ID del nuevo TipoAdjunto y Revisor
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(updateDTO.getHdo_tad_id());
            Revisor revisor = revisorService.buscarRevisorPorId(updateDTO.getHdo_rev_id());

            // Verificar si los IDs existen
            if(tipoAdjunto == null){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el TipoAdjunto con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }
            if(revisor == null){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Revisor con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar permiso isRev_aprob_emp
            if (!revisor.isRev_aprob_emp()) {
                throw new IllegalArgumentException("El revisor debe tener el campo isRev_aprob_emp en true.");
            }

            // Convertir Strings a LocalDate para las fechas en el DTO
            LocalDate fecha = LocalDate.parse(updateDTO.getHdo_adjunto_fecha());

            // Actualizar los campos de Habilitacion-Documento
            habilitacionDocumentoExistente.setTipoAdjunto(tipoAdjunto);
            habilitacionDocumentoExistente.setHdo_adjunto_orden(updateDTO.getHdo_adjunto_orden());
            habilitacionDocumentoExistente.setHdo_adjunto_fecha(fecha);
            habilitacionDocumentoExistente.setRevisor(revisor);

            //Actualizar Medio-Documento
            habilitacionDocumentoService.updateHabilitacionDocumento(habilitacionDocumentoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Habilitacion-Documento."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarHabilitacionDocumento(@PathVariable Integer id) {
        try {
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                habilitacionDocumentoService.deleteHabilitacionDocumentoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Habilitacion de Documentos eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar la  Habilitacion de Documentos. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}