package com.maticolque.apirestelevadores.controller;


import com.maticolque.apirestelevadores.dto.DistritoDTO;
import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.RevisorDTO;
import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.service.DistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/distrito")
public class DistritoController {

    @Autowired
    private DistritoService distritoService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Distritos
            List<Distrito> distritos = distritoService.getAllDistrito();

            // Verificar la lista de Distritos
            if (distritos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Distritos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<DistritoDTO> distritosDTO = new ArrayList<>();
            for (Distrito distrito : distritos) {
                DistritoDTO dto = DistritoDTO.fromEntity(distrito);
                distritosDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("distritos", distritosDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Distritos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDistritoPorId(@PathVariable Integer id) {
        try {
            Distrito distritoExistente = distritoService.buscarDistritoPorId(id);

            if (distritoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(distritoExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Distrito. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearDistrito(@RequestBody DistritoDTO distritoDTO) {
        try {
            // Validar datos
            if (distritoDTO.getDis_codigo() == null || distritoDTO.getDis_nombre().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de Distrito son obligatorios.");
            }

            // Convertir DTO a entidad
            Distrito distrito = DistritoDTO.toEntity(distritoDTO);

            // Crear Destino
            Distrito nuevoDistrito = distritoService.createDistrito(distrito);

            // Convertir entidad a DTO
            DistritoDTO nuevoDistritoDTO = DistritoDTO.fromEntity(nuevoDistrito);

            // Mandar respuesta
            RespuestaDTO<DistritoDTO> respuesta = new RespuestaDTO<>(nuevoDistritoDTO, "Distrito", "Distrito creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Distrito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Distrito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarDistrito(@PathVariable Integer id, @RequestBody DistritoDTO distritoDTO) {
        try {
            // Buscar el Distrito por ID
            Distrito distritoExistente = distritoService.buscarDistritoPorId(id);

            // Verificar si existe el ID del Distrito
            if (distritoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Distrito con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (distritoDTO.getDis_codigo().isEmpty() || distritoDTO.getDis_nombre().isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos del Distrito son obligatorios.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos del Distrito
            distritoExistente.setDis_codigo(distritoDTO.getDis_codigo());
            distritoExistente.setDis_nombre(distritoDTO.getDis_nombre());
            distritoExistente.setDis_activo(distritoDTO.isDis_activo());

            // Actualizar Distrito
            distritoService.updateDistrito(distritoExistente);

            //Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar el Distrito: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> elimimarDistrito(@PathVariable Integer id) {
        try {
            Distrito distritoExistente = distritoService.buscarDistritoPorId(id);

            if (distritoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                distritoService.deleteDistritoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Distrito eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Distrito. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}