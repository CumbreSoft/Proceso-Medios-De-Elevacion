package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/persona")
public class PersonaController {

    @Autowired
    private PersonaService personaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Persona>  personas = personaService.getAllPersona();

            if (personas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Personas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(personas);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Personas: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPersonaPorId(@PathVariable Integer id) {
        try {
            Persona personaExistente = personaService.buscarPersonaPorId(id);

            if (personaExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(personaExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Persona. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<Persona> crearPersona(@RequestBody Persona persona) {
        try {
            // Realizar validación de los datos
            if (persona.getPer_nombre().isEmpty() || persona.getPer_tipodoc() == 0
                    || persona.getPer_numdoc().isEmpty()
                    || persona.getPer_telefono().isEmpty()
                    || persona.getPer_correo().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");
            }

            // Llamar al servicio para crear la Persona
            Persona nuevaPersona= personaService.createPersona(persona);
            return new RespuestaDTO<>(nuevaPersona, "Persona creada con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Persona: " + e.getMessage());

        } catch (Exception e) {

            return new RespuestaDTO<>(null, "Error al crear una nueva Persona: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarPersona(@PathVariable Integer id, @RequestBody Persona persona) {
        try {
            // Lógica para modificar la Persona
            Persona personaExistente = personaService.buscarPersonaPorId(id);

            if (personaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            personaExistente.setPer_nombre(persona.getPer_nombre());
            personaExistente.setPer_tipodoc(persona.getPer_tipodoc());
            personaExistente.setPer_numdoc(persona.getPer_numdoc());
            personaExistente.setPer_telefono(persona.getPer_telefono());
            personaExistente.setPer_correo(persona.getPer_correo());
            personaExistente.setPer_es_dueno_emp(persona.isPer_es_dueno_emp());
            personaExistente.setPer_es_reptec_emp(persona.isPer_es_reptec_emp());
            personaExistente.setPer_es_admin_edif(persona.isPer_es_admin_edif());
            personaExistente.setPer_es_coprop_edif(persona.isPer_es_coprop_edif());
            personaExistente.setPer_activa(persona.isPer_activa());

            personaService.updatePersona(personaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar a la Persona. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Integer id) {
        try {
            Persona personaExistente = personaService.buscarPersonaPorId(id);

            if (personaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                personaService.deletePersonaById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Persona eliminada correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar a la Persona. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
