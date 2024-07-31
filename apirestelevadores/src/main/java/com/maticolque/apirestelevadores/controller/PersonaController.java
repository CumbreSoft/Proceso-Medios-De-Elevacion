package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/persona")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private InmueblePersonaService inmueblePersonaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Personas
            List<Persona> personas = personaService.getAllPersona();

            // Verificar la lista de Personas
            if (personas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Personas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<PersonaDTO> personasDTO = new ArrayList<>();
            for (Persona persona : personas) {
                PersonaDTO dto = PersonaDTO.fromEntity(persona);
                personasDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("personas", personasDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

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
    public ResponseEntity<?> crearPersona(@RequestBody PersonaDTO personaDTO) {
        try {
            // Validar datos
            if (personaDTO.getPer_nombre().isEmpty() || personaDTO.getPer_apellido().isEmpty() || personaDTO.getPer_cuit().isEmpty()
                    || personaDTO.getPer_tipodoc() == 0
                    || personaDTO.getPer_numdoc().isEmpty()
                    || personaDTO.getPer_telefono().isEmpty()
                    || personaDTO.getPer_correo().isEmpty()
                    || personaDTO.getPer_domic_legal().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de la Persona son obligatorios.");
            }

            // Convertir DTO a entidad
            Persona persona = PersonaDTO.toEntity(personaDTO);

            // Crear Persona
            Persona nuevaPersona= personaService.createPersona(persona);

            // Convertir entidad a DTO
            PersonaDTO nuevaPersonaDTO = PersonaDTO.fromEntity(nuevaPersona);

            // Mandar respuesta
            RespuestaDTO<PersonaDTO> respuesta = new RespuestaDTO<>(nuevaPersonaDTO, "Persona", "Persona creada con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear una nueva Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear una nueva Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable Integer id, @RequestBody PersonaDTO personaDTO) {
        try {
            // Buscar Persona por ID
            Persona personaExistente = personaService.buscarPersonaPorId(id);

            // Verificar si existe el ID de la Persona
            if (personaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (personaDTO.getPer_nombre().isEmpty() || personaDTO.getPer_apellido().isEmpty() || personaDTO.getPer_cuit().isEmpty()
                    || personaDTO.getPer_tipodoc() == 0
                    || personaDTO.getPer_numdoc().isEmpty()
                    || personaDTO.getPer_telefono().isEmpty()
                    || personaDTO.getPer_correo().isEmpty()
                    || personaDTO.getPer_domic_legal().isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos de la Persona son obligatorios.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos de la Persona
            personaExistente.setPer_nombre(personaDTO.getPer_nombre());
            personaExistente.setPer_apellido(personaDTO.getPer_apellido());
            personaExistente.setPer_cuit(personaDTO.getPer_cuit());
            personaExistente.setPer_tipodoc(personaDTO.getPer_tipodoc());
            personaExistente.setPer_numdoc(personaDTO.getPer_numdoc());
            personaExistente.setPer_telefono(personaDTO.getPer_telefono());
            personaExistente.setPer_correo(personaDTO.getPer_correo());
            personaExistente.setPer_domic_legal(personaDTO.getPer_domic_legal());
            personaExistente.setPer_es_dueno_emp(personaDTO.isPer_es_dueno_emp());
            personaExistente.setPer_es_reptec_emp(personaDTO.isPer_es_reptec_emp());
            personaExistente.setPer_es_admin_edif(personaDTO.isPer_es_admin_edif());
            personaExistente.setPer_es_coprop_edif(personaDTO.isPer_es_coprop_edif());
            personaExistente.setPer_activa(personaDTO.isPer_activa());

            // Actualizar Persona
            personaService.updatePersona(personaExistente);

            // Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar la Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);

        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Integer id) {
        return personaService.eliminarPersonaSiNoTieneRelaciones(id);
    }

    /*
    CON ESTA API GET "EMPRESASINMUEBLESPERSONAS", LISTO TODAS LAS EMPRESAS E INMUEBLES DE UNA PERSONA,
    POR EJEMPLO SI UNA PERSONA TIENE 3 EMPRESAS MUESTRO LA PERSONA CON SUS 3 EMPRESAS.
    */
    @GetMapping("/primeraEmpresaInmueblePersona")
    public ResponseEntity<?> obtenerDatosCompletosEeI() {

        // Crear mapa para estructurar la respuesta
        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener todos los datos de EmpresaPersona e InmueblePersona
            List<EmpresaPersona> empresaPersonas = empresaPersonaService.getAllEmpresaPersona();
            List<InmueblePersona> inmueblePersonas = inmueblePersonaService.getAllInmueblePersona();

            // Lista para combinar todas las relaciones
            List<PersonaEmpresaInmuebleDTO> combinadas = new ArrayList<>();

            // Mapas para evitar duplicados
            Map<Integer, PersonaEmpresaInmuebleDTO> personaMap = new HashMap<>();

            // Agregar datos de EmpresaPersona
            for (EmpresaPersona empresaPersona : empresaPersonas) {
                PersonaEmpresaInmuebleDTO dto = PersonaEmpresaInmuebleDTO.fromEmpresaPersona(empresaPersona);
                personaMap.put(dto.getPer_id(), dto);
            }

            // Agregar datos de InmueblePersona
            for (InmueblePersona inmueblePersona : inmueblePersonas) {
                PersonaEmpresaInmuebleDTO dto = PersonaEmpresaInmuebleDTO.fromInmueblePersona(inmueblePersona);
                if (personaMap.containsKey(dto.getPer_id())) {
                    PersonaEmpresaInmuebleDTO existingDTO = personaMap.get(dto.getPer_id());
                    // Si ya existe, actualizar el DTO
                    if (dto.getEmpresa() != null) {
                        existingDTO.setEmpresa(dto.getEmpresa());
                    }
                    if (dto.getInmueble() != null) {
                        existingDTO.setInmueble(dto.getInmueble());
                    }
                } else {
                    // Si no existe, agregar nuevo DTO
                    personaMap.put(dto.getPer_id(), dto);
                }
            }

            // Agregar todos los DTOs a la lista de respuesta
            combinadas.addAll(personaMap.values());

            // Crear la respuesta
            response.put("empresaInmueblePersona", combinadas);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista completa de Empresas, Inmuebles y Personas: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }
}