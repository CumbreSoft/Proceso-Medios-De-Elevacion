package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.DestinoService;
import com.maticolque.apirestelevadores.service.DistritoService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/inmuebles")
public class InmuebleController {

    @Autowired
    private InmuebleService inmuebleService;

    @Autowired
    private DistritoService distritoService;

    @Autowired
    private DestinoService destinoService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Inmuebles
            List<Inmueble> inmuebles = inmuebleService.getAllInmuebles();

            // Verificar la lista de Inmuebles
            if (inmuebles.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Inmuebles.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<InmuebleReadDTO> inmuebleReadDTO = new ArrayList<>();
            for (Inmueble inmueble : inmuebles) {
                InmuebleReadDTO readDTO = InmuebleReadDTO.fromEntity(inmueble);
                inmuebleReadDTO.add(readDTO);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("inmuebles", inmuebleReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmuebles: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInmueblePorId(@PathVariable Integer id) {
        try {
            Inmueble inmuebleExistente = inmuebleService.buscarInmueblePorId(id);

            if (inmuebleExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(inmuebleExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Inmueble. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearInmuble(@RequestBody InmuebleCreateDTO createdto) {
        try {

            // Validar datos
            if (createdto.getInm_padron() == 0 || createdto.getInm_direccion().isEmpty() || createdto.getInm_cod_postal().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos del Inmueble son obligatorios.");
            }
            if (createdto.getInm_dis_id() == 0) {
                throw new IllegalArgumentException("El Distrito es obligatorio.");
            } else if (createdto.getInm_dst_id() == 0) {
                throw new IllegalArgumentException("El Destino es obligatorio.");
            }

            // Buscar Distrito y Destino por sus IDs
            Distrito distrito = distritoService.buscarDistritoPorId(createdto.getInm_dis_id());
            Destino destino = destinoService.buscarDestinoPorId(createdto.getInm_dst_id());

            // Verificar si los IDs existen
            if (distrito == null || destino == null) {
                throw new IllegalArgumentException("ID de Distrito o Destino no encontrado.");
            }

            // Convertir DTO a entidad
            Inmueble inmueble = InmuebleCreateDTO.toEntity(createdto, distrito, destino);

            // Crear Inmueble
            Inmueble nuevoInmueble = inmuebleService.createInmueble(inmueble);

            // Convertir entidad a DTO
            InmuebleReadDTO nuevoInmuebleReadDTO = InmuebleReadDTO.fromEntity(nuevoInmueble);

            // Mandar respuesta
            RespuestaDTO<InmuebleReadDTO> respuesta = new RespuestaDTO<>(nuevoInmuebleReadDTO, "Inmueble", "Inmueble creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Inmueble: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Inmueble: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> modificarInmueble(@PathVariable Integer id, @RequestBody InmuebleUpdateDTO updateDTO) {
        try {
            // Buscar el Inmueble por ID
            Inmueble inmuebleExistente = inmuebleService.buscarInmueblePorId(id);

            // Verificar si existe el ID del Inmueble
            if (inmuebleExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Inmueble con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener ID del nuevo Distrito y Destino
            Distrito nuevoDistrito = distritoService.buscarDistritoPorId(updateDTO.getInm_dis_id());
            Destino nuevoDestino = destinoService.buscarDestinoPorId(updateDTO.getInm_dst_id());

            // Verificar si existe el ID del nuevo Distrito y Destino
            if (nuevoDistrito == null || nuevoDestino == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("ID de Distrito o Destino no encontrado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar los campos del Inmueble
            inmuebleExistente.setInm_padron(updateDTO.getInm_padron());
            inmuebleExistente.setInm_direccion(updateDTO.getInm_direccion());
            inmuebleExistente.setInm_cod_postal(updateDTO.getInm_cod_postal());
            inmuebleExistente.setDistrito(nuevoDistrito);
            inmuebleExistente.setDestino(nuevoDestino);
            inmuebleExistente.setInm_activo(updateDTO.isInm_activo());

            //Actualizar Inmueble
            inmuebleService.updateInmueble(inmuebleExistente);

            //Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El Inmueble se actualizó correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar el Inmueble: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<ErrorDTO> eliminarInmueble(@PathVariable int id) {
        return inmuebleService.eliminarInmuebleSiNoTieneRelaciones(id);
    }
}