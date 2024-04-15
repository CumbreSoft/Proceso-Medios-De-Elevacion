package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.service.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Inmueble> inmuebles = inmuebleService.getAllInmuebles();

            if (inmuebles.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Inmuebles.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(inmuebles);
            List<Map<String, Object>> inmueblesDTO = new ArrayList<>();
            for (Inmueble inmueble : inmuebles) {
                Map<String, Object> inmuebleMap = new LinkedHashMap<>();

                inmuebleMap.put("inm_id", inmueble.getInm_id());
                inmuebleMap.put("destino", inmueble.getDestino());
                inmuebleMap.put("inm_padron",inmueble.getInm_padron());
                inmuebleMap.put("inm_direccion",inmueble.getInm_direccion());
                inmuebleMap.put("distrito",inmueble.getDistrito());
                inmuebleMap.put("inm_cod_postal",inmueble.getInm_cod_postal());
                inmuebleMap.put("inm_activo",inmueble.isInm_activo());

                inmueblesDTO.add(inmuebleMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("inmuebles", inmueblesDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
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
            Inmueble inmuebleExistente = inmuebleService.buscarInmbublePorId(id);

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
    public RespuestaDTO<Inmueble> crearInmuble(@RequestBody Inmueble inmueble) {
        try {
            // Realizar validación de los datos
            if (inmueble.getInm_direccion().isEmpty() || inmueble.getInm_cod_postal() == null || inmueble.getInm_padron() == 0) {
                throw new IllegalArgumentException("Todos los datos del Inmueble son obligatorios.");

            }else if(inmueble.getDestino().getDst_id() == 0){

                throw new IllegalArgumentException("El Destino es obligatorio.");

            }else if(inmueble.getDistrito().getDis_id() == 0){

                throw new IllegalArgumentException("El Distrito es obligatorio.");
            }

            // Llamar al servicio para crear el Inmueble
            Inmueble nuevoInmueble = inmuebleService.createInmueble(inmueble);
            return new RespuestaDTO<>(nuevoInmueble, "Inmueble creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> modificarInmuble(@PathVariable Integer id, @RequestBody Inmueble inmueble) {
        try {
            // Lógica para modificar el Inmueble
            Inmueble destinoExistente = inmuebleService.buscarInmbublePorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            destinoExistente.setDestino(inmueble.getDestino());
            destinoExistente.setInm_padron(inmueble.getInm_padron());
            destinoExistente.setInm_direccion(inmueble.getInm_direccion());
            destinoExistente.setDistrito(inmueble.getDistrito());
            destinoExistente.setInm_cod_postal(inmueble.getInm_cod_postal());
            destinoExistente.setInm_activo(inmueble.isInm_activo());

            inmuebleService.updateInmueble(destinoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Inmueble."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarInmuble(@PathVariable Integer id) {
        try {
            Inmueble inmuebleExistente = inmuebleService.buscarInmbublePorId(id);

            if (inmuebleExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                inmuebleService.deleteInmuebleById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Inmueble eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Inmueble. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}

