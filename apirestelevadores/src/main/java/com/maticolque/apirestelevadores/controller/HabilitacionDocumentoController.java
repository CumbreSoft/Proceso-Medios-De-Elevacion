package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.HabilitacionDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("api/v1/habilitacionDocumento")
public class HabilitacionDocumentoController {

    @Autowired
    private HabilitacionDocumentoService habilitacionDocumentoService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<HabilitacionDocumento> habilitacionDocumentos = habilitacionDocumentoService.getAllHabilitacionDocumento();

            if (habilitacionDocumentos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Habilitacion de Documentos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(habilitacionDocumentos);
        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Habilitacion de Documentos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarHabilitacionDocumentoPorId(@PathVariable Integer id) {
        try {
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(habilitacionDocumentoExistente);
            }

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
    public RespuestaDTO<HabilitacionDocumento> crearHabilitacionDocumento(@RequestBody HabilitacionDocumento habilitacionDocumento) {
        try {
            // Realizar validación de los datos
            if (habilitacionDocumento == null ||
                    habilitacionDocumento.getHdo_adjunto_orden() == 0 ||
                    habilitacionDocumento.getHdo_adjunto_fecha() == null ) {

                throw new IllegalArgumentException("Todos los datos de la Habilitacion de Documentos son obligatorios.");

            }else if(habilitacionDocumento.getEmpresaHabilitacion().getEha_id() == 0){

                throw new IllegalArgumentException("La Empresa de Habilitacion es obligatoria.");

            }else if(habilitacionDocumento.getTipoAdjunto().getTad_id() == 0){

                throw new IllegalArgumentException("El Tipo de Adjunto es obligatoria.");

            }else if(habilitacionDocumento.getRevisor().getRev_id() == 0){

                throw new IllegalArgumentException("El Revisor es obligatorio.");

            }

            // Llamar al servicio para crear Habilitación Documentos
            HabilitacionDocumento nuevoHabilitacionDocumento = habilitacionDocumentoService.createHabilitacionDocumento(habilitacionDocumento);
            return new RespuestaDTO<>(nuevoHabilitacionDocumento, "Habilitacion de Documentos creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Habilitacion de Documentos: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Habilitacion de Documentos: " + e.getMessage());
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarHabilitacionDocumento(@PathVariable Integer id, @RequestBody HabilitacionDocumento habilitacionDocumento) {
        try {
            // Lógica para modificar Habilitación Documentos
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            habilitacionDocumentoExistente.setEmpresaHabilitacion(habilitacionDocumento.getEmpresaHabilitacion());
            habilitacionDocumentoExistente.setTipoAdjunto(habilitacionDocumento.getTipoAdjunto());
            habilitacionDocumentoExistente.setHdo_adjunto_orden(habilitacionDocumento.getHdo_adjunto_orden());
            habilitacionDocumentoExistente.setHdo_adjunto_fecha(habilitacionDocumento.getHdo_adjunto_fecha());
            habilitacionDocumentoExistente.setRevisor(habilitacionDocumento.getRevisor());

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
                    .message("Error al modificar la Habilitacion de Documentos."+ e.getMessage())
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
