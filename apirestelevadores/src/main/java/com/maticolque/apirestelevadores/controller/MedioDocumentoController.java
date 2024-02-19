package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/medioDocumento")
public class MedioDocumentoController {

    @Autowired
    private MedioDocumentoService medioDocumentoService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<MedioDocumento> mediosDocumento = medioDocumentoService.getAllMedioDocumento();

            if (mediosDocumento.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medios Documento.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(mediosDocumento);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios Documento: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioDocumentoPorId(@PathVariable Integer id) {
        try {
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                return ResponseEntity.ok(medioDocumentoExistente);
            }

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
    public RespuestaDTO<MedioDocumento> crearMedioDocumento(@RequestBody MedioDocumento medioDocumento) {
        try {
            // Realizar validación de los datos
            if(medioDocumento.getMdo_adjunto_orden() == 0 || medioDocumento.getMdo_adjunto_fecha() == null){
                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");
            }
            else if (medioDocumento.getMedioHabilitacion().getMha_id() == 0) {
                throw new IllegalArgumentException("El Medio de Habilitación es obligatorio.");
            }
            else if(medioDocumento.getTipoAdjunto().getTad_id() == 0){
                throw new IllegalArgumentException("El Tipo de Adjunto es obligatorio.");
            }
            else if(medioDocumento.getRevisor().getRev_id() == 0){
                throw new IllegalArgumentException("El Revisor es obligatorio.");
            }

            // Llamar al servicio para crear el destino
            MedioDocumento nuevoMedioDocumento= medioDocumentoService.createMedioDocumento(medioDocumento);
            return new RespuestaDTO<>(nuevoMedioDocumento, "Medio Documento creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo destino: " + e.getMessage());
        } catch (Exception e) {
            // Capturar otras excepciones
            e.printStackTrace();
            return new RespuestaDTO<>(null, "Error al crear un nuevo destino: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarMedioDocumento(@PathVariable Integer id, @RequestBody MedioDocumento medioDocumento) {
        try {
            // Lógica para modificar el Medio Documento
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioDocumentoExistente.setMedioHabilitacion(medioDocumento.getMedioHabilitacion());
            medioDocumentoExistente.setTipoAdjunto(medioDocumento.getTipoAdjunto());
            medioDocumentoExistente.setMdo_adjunto_orden(medioDocumento.getMdo_adjunto_orden());
            medioDocumentoExistente.setMdo_adjunto_fecha(medioDocumento.getMdo_adjunto_fecha());
            medioDocumentoExistente.setRevisor(medioDocumento.getRevisor());

            // Agrega más propiedades según tu modelo
            medioDocumentoService.updateMedioDocumento(medioDocumentoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Medio Documento. " + e.getMessage())
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
