package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/inmueblePersona")
public class InmueblePersonaController {

    @Autowired
    private InmueblePersonaService inmueblePersonaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<InmueblePersona> inmueblePersona = inmueblePersonaService.getAllInmueblePersona();

            if (inmueblePersona.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Immuebles de las Personas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(inmueblePersona);
        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista Inmueble Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInmueblePersona(@PathVariable Integer id) {
        try {
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            if (inmueblePersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(inmueblePersonaExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Inmueble de la Persona. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<InmueblePersona> crearInmublePersona(@RequestBody InmueblePersona inmueblePersona) {
        try {
            // Realizar validación de los datos
            if (inmueblePersona.getInmueble().getInm_id() == 0) {

                throw new IllegalArgumentException("El Inmueble es obligatorio.");

            }else if (inmueblePersona.getPersona().getPer_id() == 0){

                throw new IllegalArgumentException("La Persona es obligatoria.");
            }

            // Llamar al servicio para crear el destino
            InmueblePersona inmueblePersonaDestino = inmueblePersonaService.createInmueblePersona(inmueblePersona);
            return new RespuestaDTO<>(inmueblePersonaDestino, "Inmueble Persona creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble Persona: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble Persona: "+ e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarInmueblePersona(@PathVariable Integer id, @RequestBody InmueblePersona inmueblePersona) {
        try {
            // Lógica para modificar el Inmueble Persona
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            if (inmueblePersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            inmueblePersonaExistente.setIpe_es_admin_edif(inmueblePersona.isIpe_es_admin_edif());
            inmueblePersonaExistente.setIpe_es_coprop_edif(inmueblePersona.isIpe_es_coprop_edif());
            inmueblePersonaExistente.setInmueble(inmueblePersona.getInmueble());
            inmueblePersonaExistente.setPersona(inmueblePersona.getPersona());

            inmueblePersonaService.updateInmueblePersona(inmueblePersonaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Inmueble Persona."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> elimimarInmueblePersona(@PathVariable Integer id) {
        try {
            InmueblePersona destinoExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                inmueblePersonaService.deleteInmueblePersonaById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Inmueble Persona eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Inmueble Persona. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
