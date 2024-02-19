package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.service.InmuebleMedioElevacionService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/inmueblesMDE")
public class InmuebleMedioElevacionController {

    @Autowired
    private InmuebleMedioElevacionService inmuebleMedioElevacionService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<InmuebleMedioElevacion> medioElevacions = inmuebleMedioElevacionService.getAllInmueblesMDE();

            if (medioElevacions.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Inmuebles Medios de Elevación.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(medioElevacions);
        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmueble Medios de Elevación: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInmuebleMDEPorId(@PathVariable Integer id) {
        try {
            InmuebleMedioElevacion medioElevacion = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (medioElevacion == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(medioElevacion);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Inmueble Medios de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<InmuebleMedioElevacion> crearInmuebleMDE(@RequestBody InmuebleMedioElevacion inmuebleMedioElevacion) {
        // Llamar al servicio para crear el Inmueble Medios de Elevación

        try {
            // Realizar validación de los datos
             if(inmuebleMedioElevacion.getInmueble().getInm_id() == 0){

                 throw new IllegalArgumentException("El Inmueble es obligatorio.");

            }else if(inmuebleMedioElevacion.getMedioElevacion().getMde_id() == 0){

                 throw new IllegalArgumentException("El  Medio de Elevacion es obligatorio.");

             }

            // Llamar al servicio para crear el Inmueble Medios de Elevación
            InmuebleMedioElevacion nuevoInmuebleMedioElevacion = inmuebleMedioElevacionService.createInmuebleMDE(inmuebleMedioElevacion);
            return new RespuestaDTO<>(nuevoInmuebleMedioElevacion, "Inmueble Medio de Elevación creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Elevación: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Elevación:  " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> modificarInmuebleMDE(@PathVariable Integer id, @RequestBody InmuebleMedioElevacion inmuebleMedioElevacion) {
        try {
            // Lógica para modificar el Inmueble Medios de Elevación
            InmuebleMedioElevacion medioElevacionExistente = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (medioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioElevacionExistente.setInmueble(inmuebleMedioElevacion.getInmueble());
            medioElevacionExistente.setMedioElevacion(inmuebleMedioElevacion.getMedioElevacion()  );

            inmuebleMedioElevacionService.updateInmuebleMDE(medioElevacionExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Inmueble Medio de Elevacion."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarInmuebleMDE(@PathVariable Integer id) {
        try {
            InmuebleMedioElevacion destinoExistente = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                inmuebleMedioElevacionService.deleteInmuebleMDEById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Inmueble Medio de Elevacion eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Inmueble Medio de Elevacion. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
