package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.TipoAdjuntoDTO;
import com.maticolque.apirestelevadores.model.TipoAdjunto;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
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
            // Obtener la lista de TipoAdjunto
            List<TipoAdjunto> tipoAdjuntos = tipoAdjuntoService.getAllTipoAdjunto();

            // Verificar la lista de TipoAdjunto
            if (tipoAdjuntos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron TipoAdjunto.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<TipoAdjuntoDTO> tipoAdjuntosDTO = new ArrayList<>();
            for (TipoAdjunto tipoAdjunto : tipoAdjuntos) {
                TipoAdjuntoDTO dto = TipoAdjuntoDTO.fromEntity(tipoAdjunto);
                tipoAdjuntosDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("tiposAdjuntos", tipoAdjuntosDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de TipoAdjunto: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoAdjuntoPorId(@PathVariable Integer id) {
        try {

            // Buscar TipoAdjunto por ID
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            // Verificar si existe el ID
            if (tipoAdjuntoExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            }

            // Convertir la entidad en un DTO
            TipoAdjuntoDTO tipoAdjuntoDTO = TipoAdjuntoDTO.fromEntity(tipoAdjuntoExistente);

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("tipoAdjunto", tipoAdjuntoDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Tipo de Adjunto. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearTipoAdjunto(@RequestBody TipoAdjuntoDTO tipoAdjuntoDTO){
        try {

            // Validar datos
            if (tipoAdjuntoDTO.getTad_nombre().isEmpty() || tipoAdjuntoDTO.getTad_cod() == 0) {
                throw new IllegalArgumentException("No se permiten datos vacíos.");
            }

            // Convertir DTO a entidad
            TipoAdjunto tipoAdjunto = TipoAdjuntoDTO.toEntity(tipoAdjuntoDTO);

            // Crear TipoAdjunto
            TipoAdjunto nuevoTipoAdjunto = tipoAdjuntoService.createTipoAdjunto(tipoAdjunto);

            // Convertir entidad a DTO
            TipoAdjuntoDTO nuevoTipoAdjuntoDTO = TipoAdjuntoDTO.fromEntity(nuevoTipoAdjunto);

            // Mandar respuesta
            RespuestaDTO<TipoAdjuntoDTO> respuesta = new RespuestaDTO<>(nuevoTipoAdjuntoDTO, "TipoAdjunto", "Tipo de Adjunto creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo TipoAdjunto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo TipoAdjunto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarTipoAdjunto(@PathVariable Integer id, @RequestBody TipoAdjuntoDTO tipoAdjuntoDTO) {
        try {

            // Buscar el TipoAdjunto por ID
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            // Verificar si existe el ID del TipoAdjunto
            if (tipoAdjuntoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el TipoAdjunto con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (tipoAdjuntoDTO.getTad_nombre().isEmpty() || tipoAdjuntoDTO.getTad_cod() == 0) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("No se permiten datos vacíos.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos del TipoAdjunto
            tipoAdjuntoExistente.setTad_nombre(tipoAdjuntoDTO.getTad_nombre());
            tipoAdjuntoExistente.setTad_cod(tipoAdjuntoDTO.getTad_cod());
            tipoAdjuntoExistente.setTad_activo(tipoAdjuntoDTO.isTad_activo());

            // Actualizar TipoAdjunto
            tipoAdjuntoService.updateTipoAdjunto(tipoAdjuntoExistente);

            // Mandar respuesta
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