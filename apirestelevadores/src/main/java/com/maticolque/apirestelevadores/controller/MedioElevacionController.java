package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/medioElevacion")
public class MedioElevacionController {

    @Autowired
    private MedioElevacionService medioElevacionService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<MedioElevacion> mediosElevacion = medioElevacionService.getAllMedioElevacion();

            if (mediosElevacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medios de Elevación.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(mediosElevacion);
        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de destinos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioElevacionPorId(@PathVariable Integer id) {
        try {
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {

                return ResponseEntity.ok(medioElevacionExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Medio de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<MedioElevacion> crearMedioElevacion(@RequestBody MedioElevacion medioElevacion) {
        try {
            // Realizar validación de los datos
            if (medioElevacion.getMde_ubicacion().isEmpty() || medioElevacion.getMde_tipo().isEmpty()
                    || medioElevacion.getMde_niveles() == 0) {

                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");
            }
            else if (medioElevacion.getTiposMaquinas().getTma_id() == 0) {
                throw new IllegalArgumentException("La Máquina es obligatoria.");
            }

            // Llamar al servicio para crear el destino
            MedioElevacion nuevoMedioElevacion = medioElevacionService.createMedioElevacion(medioElevacion);

            return new RespuestaDTO<>(nuevoMedioElevacion, "Medio de Elevación creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un Medio de Elevación: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un Medio de Elevación: " + e.getMessage());
        }
    }



















    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarMedioElevacion(@PathVariable Integer id, @RequestBody MedioElevacion medioElevacion) {
        try {
            // Lógica para modificar el Medio de Elevación
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el medio de elevación con el ID proporcionado");
            }

            //Modificar valores
            medioElevacionExistente.setMde_ubicacion(medioElevacion.getMde_ubicacion());
            medioElevacionExistente.setMde_tipo(medioElevacion.getMde_tipo());
            medioElevacionExistente.setMde_niveles(medioElevacion.getMde_niveles());
            medioElevacionExistente.setMde_activo(medioElevacion.isMde_activo());
            medioElevacionExistente.setTiposMaquinas(medioElevacion.getTiposMaquinas());

            // Agrega más propiedades según tu modelo
            medioElevacionService.updateMedioElevacion(medioElevacionExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el medio de elevación");
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarMedioElevacion(@PathVariable Integer id) {
        try {
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                medioElevacionService.deleteMedioElevacionById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Medio de Elevación eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Medio de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


}