package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.PersonaService;
import jakarta.persistence.EntityNotFoundException;
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
    PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarInmueblePersonas() {
        try {
            List<InmueblePersona> inmueblePersonas = inmueblePersonaService.getAllInmueblePersona();
            List<Map<String, Object>> inmueblePersonasDTO = new ArrayList<>();

            for (InmueblePersona inmueblePersona : inmueblePersonas) {
                Map<String, Object> inmueblePersonaMap = new LinkedHashMap<>();
                inmueblePersonaMap.put("ipe_id", inmueblePersona.getIpe_id());
                inmueblePersonaMap.put("ipe_es_admin_edif", inmueblePersona.isIpe_es_admin_edif());
                inmueblePersonaMap.put("ipe_es_coprop_edif", inmueblePersona.isIpe_es_coprop_edif());

                // Extraer los datos del inmueble asociado
                Inmueble inmueble = inmueblePersona.getInmueble();
                Map<String, Object> inmuebleMap = new LinkedHashMap<>();
                inmuebleMap.put("inm_id", inmueble.getInm_id());
                inmuebleMap.put("inm_padron", inmueble.getInm_padron());
                inmuebleMap.put("destino", inmueble.getDestino());
                inmuebleMap.put("inm_direccion", inmueble.getInm_direccion());
                inmuebleMap.put("distrito", inmueble.getDistrito());
                inmuebleMap.put("inm_cod_postal", inmueble.getInm_cod_postal());
                inmuebleMap.put("inm_activo", inmueble.isInm_activo());
                inmueblePersonaMap.put("inmueble", inmuebleMap);

                // Extraer los datos de la persona asociada
                Persona persona = inmueblePersona.getPersona();
                Map<String, Object> personaMap = new LinkedHashMap<>();
                personaMap.put("per_id", persona.getPer_id());
                personaMap.put("per_nombre", persona.getPer_nombre());
                personaMap.put("per_apellido", persona.getPer_apellido());
                personaMap.put("per_cuit", persona.getPer_cuit());
                personaMap.put("per_tipodoc", persona.getPer_tipodoc());
                personaMap.put("per_numdoc", persona.getPer_numdoc());
                personaMap.put("per_telefono", persona.getPer_telefono());
                personaMap.put("per_correo", persona.getPer_correo());
                personaMap.put("per_domic_legal", persona.getPer_domic_legal());
                personaMap.put("per_es_dueno_emp", persona.isPer_es_dueno_emp());
                personaMap.put("per_es_reptec_emp", persona.isPer_es_reptec_emp());
                personaMap.put("per_es_admin_edif", persona.isPer_es_admin_edif());
                personaMap.put("per_es_coprop_edif", persona.isPer_es_coprop_edif());
                personaMap.put("per_activa", persona.isPer_activa());
                inmueblePersonaMap.put("persona", personaMap);

                inmueblePersonasDTO.add(inmueblePersonaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("inmueblePersonas", inmueblePersonasDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmueble Persona: " + e.getMessage())
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
    public RespuestaDTO<InmueblePersona> crearInmueblePersona(@RequestBody Map<String, Object> requestData) {
        try {
            Integer inmuebleId = (Integer) requestData.get("inmuebleId");
            Integer personaId = (Integer) requestData.get("personaId");
            Boolean ipeEsAdminEdif = (Boolean) requestData.get("ipe_es_admin_edif");
            Boolean ipeEsCopropEdif = (Boolean) requestData.get("ipe_es_coprop_edif");

            // Validaciones
            if (inmuebleId == null) {
                throw new IllegalArgumentException("El ID del inmueble es obligatorio.");
            }
            if (personaId == null) {
                throw new IllegalArgumentException("El ID de la persona es obligatorio.");
            }

            // Buscar el inmueble por su ID
            Inmueble inmueble = inmuebleService.buscarInmueblePorId(inmuebleId);
            if (inmueble == null) {
                throw new IllegalArgumentException("No se encontró el inmueble con el ID especificado.");
            }

            // Buscar la persona por su ID
            Persona persona = personaService.buscarPersonaPorId(personaId);
            if (persona == null) {
                throw new IllegalArgumentException("No se encontró la persona con el ID especificado.");
            }

            // Crear la entidad de InmueblePersona y establecer las relaciones
            InmueblePersona inmueblePersona = new InmueblePersona();
            inmueblePersona.setInmueble(inmueble);
            inmueblePersona.setPersona(persona);
            inmueblePersona.setIpe_es_admin_edif(ipeEsAdminEdif != null ? ipeEsAdminEdif : false);
            inmueblePersona.setIpe_es_coprop_edif(ipeEsCopropEdif != null ? ipeEsCopropEdif : false);

            // Crear la relación Inmueble-Persona
            InmueblePersona nuevoInmueblePersona = inmueblePersonaService.createInmueblePersona(inmueblePersona);

            // Actualizar los valores de per_es_admin_edif y per_es_coprop_edif en la entidad Persona
            persona.setPer_es_admin_edif(ipeEsAdminEdif != null ? ipeEsAdminEdif : false);
            persona.setPer_es_coprop_edif(ipeEsCopropEdif != null ? ipeEsCopropEdif : false);

            // Guardar la persona actualizada en la base de datos
            personaRepository.save(persona);

            return new RespuestaDTO<>(nuevoInmueblePersona, "Inmueble Persona creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble Persona: " + e.getMessage());
        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Inmueble Persona: " + e.getMessage());
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


    //PUT SOLO PARA CAMBIAR EL INMUEBLE DE UNA PERSONA
    @PutMapping("/editarInmuebleDePersona/{id}")
    public ResponseEntity<?> actualizarInmuebleDePersona(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Obtener el ID del nuevo Inmueble
            Integer nuevoInmuebleId = (Integer) requestBody.get("inm_id");

            // Obtener los valores de ipe_es_admin_edif y ipe_es_coprop_edif
            boolean esAdmin = (boolean) requestBody.getOrDefault("ipe_es_admin_edif", false);
            boolean esCopro = (boolean) requestBody.getOrDefault("ipe_es_coprop_edif", false);

            // Verificar si la relación InmueblePersona existe para la persona específica
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);
            if (inmueblePersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró una relación Inmueble-Persona para la persona con ID: " + id)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener el nuevo inmueble
            Inmueble nuevoInmueble = inmuebleService.buscarInmueblePorId(nuevoInmuebleId);
            if (nuevoInmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el inmueble con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar el inmueble de la persona en la relación InmueblePersona
            inmueblePersonaExistente.setInmueble(nuevoInmueble);

            // Actualizar los valores de ipe_es_admin_edif y ipe_es_coprop_edif en la relación InmueblePersona
            inmueblePersonaExistente.setIpe_es_admin_edif(esAdmin);
            inmueblePersonaExistente.setIpe_es_coprop_edif(esCopro);

            // Actualizar los valores de ipe_es_admin_edif y ipe_es_coprop_edif en la entidad Persona
            Persona personaExistente = inmueblePersonaExistente.getPersona();
            personaExistente.setPer_es_admin_edif(esAdmin);
            personaExistente.setPer_es_coprop_edif(esCopro);

            // Actualizar la relación InmueblePersona y la entidad Persona en la base de datos
            inmueblePersonaService.updateInmueblePersona(inmueblePersonaExistente);
            personaService.updatePersona(personaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El inmueble y los roles de la persona se han actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar el inmueble y los roles de la persona: " + e.getMessage())
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
