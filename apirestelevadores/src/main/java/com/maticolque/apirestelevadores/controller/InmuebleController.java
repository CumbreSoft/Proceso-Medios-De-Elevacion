package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.InmuebleRepository;
import com.maticolque.apirestelevadores.service.DestinoService;
import com.maticolque.apirestelevadores.service.DistritoService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import jakarta.annotation.PostConstruct;
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

    @Autowired
    private DistritoService distritoService;

    @Autowired
    private DestinoService destinoService;


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
                inmuebleMap.put("inm_padron",inmueble.getInm_padron());
                inmuebleMap.put("inm_direccion",inmueble.getInm_direccion());
                inmuebleMap.put("inm_cod_postal",inmueble.getInm_cod_postal());
                inmuebleMap.put("distrito",inmueble.getDistrito());
                inmuebleMap.put("destino", inmueble.getDestino());
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
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> modificarInmueble(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Verificar si el inmueble existe
            Inmueble inmuebleExistente = inmuebleService.buscarInmueblePorId(id);
            if (inmuebleExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ErrorDTO.builder()
                                .code("404 NOT FOUND")
                                .message("El Inmueble con el ID proporcionado no existe.")
                                .build()
                );
            }

            // Obtener el ID del distrito, destino, y padrón para actualizar
            Integer nuevoDistritoId = (Integer) requestBody.get("dis_id");
            Integer nuevoDestinoId = (Integer) requestBody.get("dst_id");
            Integer nuevoPadron = (Integer) requestBody.get("inm_padron");

            // Obtener otros campos para actualizar
            String nuevaDireccion = (String) requestBody.get("inm_direccion");
            String nuevoCodPostal = (String) requestBody.get("inm_cod_postal");
            Boolean nuevoInmActivo = (Boolean) requestBody.get("inm_activo");

            // Verificar que al menos un campo se proporciona para actualizar
            if (nuevoDistritoId == null && nuevoDestinoId == null && nuevoPadron == null
                    && nuevaDireccion == null && nuevoCodPostal == null
                    && nuevoInmActivo == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ErrorDTO.builder()
                                .code("400 BAD REQUEST")
                                .message("Debe proporcionar al menos un campo para actualizar.")
                                .build()
                );
            }

            StringBuilder mensajeExito = new StringBuilder("Actualización exitosa:");

            // Verificar y actualizar el Distrito
            if (nuevoDistritoId != null) {
                Distrito nuevoDistrito = distritoService.buscarDistritoPorId(nuevoDistritoId);
                if (nuevoDistrito == null) {
                    mensajeExito.append(" (Error: No se encontró el Distrito con el ID proporcionado).");
                } else {
                    inmuebleExistente.setDistrito(nuevoDistrito);
                    mensajeExito.append(" Distrito actualizado correctamente.");
                }
            }

            // Verificar y actualizar el Destino
            if (nuevoDestinoId != null) {
                Destino nuevoDestino = destinoService.buscarDestinoPorId(nuevoDestinoId);
                if (nuevoDestino == null) {
                    mensajeExito.append(" (Error: No se encontró el Destino con el ID proporcionado).");
                } else {
                    inmuebleExistente.setDestino(nuevoDestino);
                    mensajeExito.append(" Destino actualizado correctamente.");
                }
            }

            // Verificar y actualizar el Padrón
            if (nuevoPadron != null) {
                inmuebleExistente.setInm_padron(nuevoPadron);
                mensajeExito.append(" Padrón actualizado correctamente.");
            }

            // Actualizar otros campos
            if (nuevaDireccion != null) {
                inmuebleExistente.setInm_direccion(nuevaDireccion);
                mensajeExito.append(" Dirección actualizada correctamente.");
            }

            if (nuevoCodPostal != null) {
                inmuebleExistente.setInm_cod_postal(nuevoCodPostal);
                mensajeExito.append(" Código postal actualizado correctamente.");
            }

            if (nuevoInmActivo != null) {
                inmuebleExistente.setInm_activo(nuevoInmActivo);
                mensajeExito.append(" Estado de actividad actualizado correctamente.");
            }

            // Guardar cambios
            inmuebleService.updateInmueble(inmuebleExistente);

            // Mensaje de éxito
            return ResponseEntity.status(HttpStatus.OK).body(
                    ErrorDTO.builder()
                            .code("200 OK")
                            .message(mensajeExito.toString())
                            .build()
            );

        } catch (Exception e) {
            // Manejo de errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorDTO.builder()
                            .code("500 INTERNAL SERVER ERROR")
                            .message("Error al actualizar el Inmueble: " + e.getMessage())
                            .build()
            );
        }
    }







    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarInmuble(@PathVariable Integer id) {
        try {
            Inmueble inmuebleExistente = inmuebleService.buscarInmueblePorId(id);

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

