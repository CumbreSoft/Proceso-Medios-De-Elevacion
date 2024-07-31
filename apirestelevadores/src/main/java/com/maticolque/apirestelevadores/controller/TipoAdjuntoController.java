package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.TipoAdjuntoDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.TipoAdjunto;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/tipoAdjunto")
public class TipoAdjuntoController {

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<TipoAdjunto> tipoAdjuntos = tipoAdjuntoService.getAllTipoAdjunto();

            if (tipoAdjuntos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Tipos de Adjuntos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<TipoAdjuntoDTO> tipoAdjuntosDTO = new ArrayList<>();
            for (TipoAdjunto tipoAdjunto : tipoAdjuntos) {
                TipoAdjuntoDTO dto = TipoAdjuntoDTO.fromEntity(tipoAdjunto);
                tipoAdjuntosDTO.add(dto);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("tiposAdjuntos", tipoAdjuntosDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Tipos de Adjuntos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoAdjuntoPorId(@PathVariable Integer id) {
        try {
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            if (tipoAdjuntoExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(tipoAdjuntoExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Tipo de Adjunto. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    /*
    @PostMapping
    public RespuestaDTO<TipoAdjuntoDTO> crearTipoAdjunto(@RequestBody TipoAdjuntoDTO tipoAdjuntoDTO){
        try {
            // Realizar validación de los datos
            if (tipoAdjuntoDTO.getTad_nombre().isEmpty() || tipoAdjuntoDTO.getTad_cod() == 0) {
                throw new IllegalArgumentException("Todos los datos de Tipo de Adjunto son obligatorios.");
            }

            // Convertir DTO a entidad
            TipoAdjunto tipoAdjunto = TipoAdjuntoDTO.toEntity(tipoAdjuntoDTO);

            // Llamar al servicio para crear el Tipo de Adjunto
            TipoAdjunto nuevoTipoAdjunto = tipoAdjuntoService.createTipoAdjunto(tipoAdjunto);

            // Convertir entidad a DTO
            TipoAdjuntoDTO nuevoTipoAdjuntoDTO = TipoAdjuntoDTO.fromEntity(nuevoTipoAdjunto);

            return new RespuestaDTO<>(nuevoTipoAdjuntoDTO, "Tipo de Adjunto creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Tipo de Adjunto: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Tipo de Adjunto: " + e.getMessage());
        }

    }*/


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarTipoAdjunto(@PathVariable Integer id, @RequestBody TipoAdjuntoDTO tipoAdjuntoDTO) {
        try {
            // Lógica para modificar el Tipo de Adjunto
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            if (tipoAdjuntoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            tipoAdjuntoExistente.setTad_nombre(tipoAdjuntoDTO.getTad_nombre());
            tipoAdjuntoExistente.setTad_cod(tipoAdjuntoDTO.getTad_cod());
            tipoAdjuntoExistente.setTad_activo(tipoAdjuntoDTO.isTad_activo());

            tipoAdjuntoService.updateTipoAdjunto(tipoAdjuntoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Tipo Adjunto. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarTipoAdjunto(@PathVariable Integer id){

        try {
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            if (tipoAdjuntoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                tipoAdjuntoService.deleteTipoAdjuntoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Tipo de Adjunto eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Tipo de Adjunto. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }

    }
}
