package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/inmueblePersona")
public class InmueblePersonaController {

    @Autowired
    private InmueblePersonaService inmueblePersonaService;

    @Autowired
    private InmuebleService inmuebleService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarInmueblePersonas() {
        try {

            // Obtener la lista de Inmueble-Persona
            List<InmueblePersona> inmueblePersonas = inmueblePersonaService.getAllInmueblePersona();

            // Verificar la lista de Inmueble-Persona
            if (inmueblePersonas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Inmueble-Persona.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<InmueblePersonaReadDTO> inmueblePersonasReadDTO = new ArrayList<>();
            for (InmueblePersona inmueblePersona : inmueblePersonas) {
                InmueblePersonaReadDTO readDTO = InmueblePersonaReadDTO.fromEntity(inmueblePersona);
                inmueblePersonasReadDTO.add(readDTO);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("inmueblePersonas", inmueblePersonasReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmueble-Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInmueblePersona(@PathVariable Integer id) {
        try {

            // Buscar InmueblePersona por ID
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            // Verificar si existe el ID
            if (inmueblePersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            }

            // Convertir la entidad en un DTO
            InmueblePersonaReadDTO inmueblePersonaReadDTO = InmueblePersonaReadDTO.fromEntity(inmueblePersonaExistente);

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put(" inmueblePersona", inmueblePersonaReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

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
    public ResponseEntity<?> crearInmueblePersona(@RequestBody InmueblePersonaCreateDTO createDto) {
        try {

            // Buscar Inmueble y Persona por sus IDs
            Inmueble inmueble = inmuebleService.buscarInmueblePorId(createDto.getIpe_inm_id());
            Persona persona = personaService.buscarPersonaPorId(createDto.getIpe_per_id());

            // Verificar si los IDs existen
            if (inmueble == null) {
                throw new IllegalArgumentException("ID de Inmueble no encontrado.");
            }
            if(persona == null){
                throw new IllegalArgumentException("ID de Persona no encontrado.");
            }

            // Convertir DTO a entidad
            InmueblePersona inmueblePersona = InmueblePersonaCreateDTO.toEntity(createDto, inmueble, persona);

            // Crear la relación InmueblePersona
            InmueblePersona nuevoInmueblePersona = inmueblePersonaService.createInmueblePersona(inmueblePersona);

            InmueblePersonaReadDTO nuevoInmueblePersonaReadDTO = InmueblePersonaReadDTO.fromEntity(nuevoInmueblePersona);

            // Actualizar los valores de per_es_dueno_emp y per_es_reptec_emp en Persona
            persona.setPer_es_admin_edif(createDto.isIpe_es_admin_edif());
            persona.setPer_es_coprop_edif(createDto.isIpe_es_coprop_edif());

            // Guardar la persona actualizada en la base de datos
            personaRepository.save(persona);

            // Mandar respuesta
            RespuestaDTO<InmueblePersonaReadDTO> respuesta = new RespuestaDTO<>(nuevoInmueblePersonaReadDTO, "Inmueble-Persona", "Inmueble-Persona creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Inmueble-Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Inmueble-Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT SOLO PARA CAMBIAR EL INMUEBLE DE UNA PERSONA
    @PutMapping("/editarInmuebleDePersona/{id}")
    public ResponseEntity<?> actualizarInmuebleDePersona(@PathVariable Integer id, @RequestBody InmueblePersonaUpdateDTO updateDTO) {
        try {

            // Obtener ID de Inmueble-Persona
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            // Verificar si existe el ID de Inmueble-Persona
            if (inmueblePersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Inmueble-Persona con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener ID del nuevo Inmueble
            Inmueble nuevoInmueble = inmuebleService.buscarInmueblePorId(updateDTO.getIpe_inm_id());

            // Verificar si existe del nuevo Inmueble
            if (nuevoInmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Inmueble con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar los campos de Inmueble-Persona
            inmueblePersonaExistente.setIpe_es_admin_edif(updateDTO.isIpe_es_admin_edif());
            inmueblePersonaExistente.setIpe_es_coprop_edif(updateDTO.isIpe_es_coprop_edif());
            inmueblePersonaExistente.setInmueble(nuevoInmueble);

            // Actualizar los valores de ipe_es_admin_edif y ipe_es_coprop_edif en la entidad Persona
            Persona personaExistente = inmueblePersonaExistente.getPersona();
            personaExistente.setPer_es_admin_edif(updateDTO.isIpe_es_admin_edif());
            personaExistente.setPer_es_coprop_edif(updateDTO.isIpe_es_coprop_edif());

            //Actualizar Inmueble-Persona
            inmueblePersonaService.updateInmueblePersona(inmueblePersonaExistente);

            //Actualizar Persona
            personaService.updatePersona(personaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El Inmueble y los roles de la persona se han actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar el Inmueble y los roles de la Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
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