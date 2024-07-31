package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.dtosimple.EmpresaPersonaSimpleDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.EmpresaService;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/empresaPersona")
public class EmpresaPersonaController {

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarEmpresaPersonas() {
        try {

            // Obtener la lista de Empresa-Persona
            List<EmpresaPersona> empresaPersonas = empresaPersonaService.getAllEmpresaPersona();

            // Verificar la lista de Empresa-Persona
            if (empresaPersonas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresa-Persona.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<EmpresaPersonaReadDTO> empresaPersonasReadDTO = new ArrayList<>();
            for (EmpresaPersona empresaPersona : empresaPersonas) {
                EmpresaPersonaReadDTO readDTO = EmpresaPersonaReadDTO.fromEntity(empresaPersona);
                empresaPersonasReadDTO.add(readDTO);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("empresaPersonas", empresaPersonasReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresa-Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaPersonaPorId(@PathVariable Integer id) {
        try {
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

            if (empresaPersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(empresaPersonaExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Empresa-Persona." + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearEmpresaPersona(@RequestBody EmpresaPersonaCreateDTO createDto) {
        try {
            // Validar datos
            if (createDto.getEpe_emp_id() == 0) {
                throw new IllegalArgumentException("La Empresa es obligatoria.");
            } else if (createDto.getEpe_per_id() == 0) {
                throw new IllegalArgumentException("La Persona es obligatoria.");
            }

            // Buscar Empresa y Persona por sus IDs
            Empresa empresa = empresaService.buscarEmpresaPorId(createDto.getEpe_emp_id());
            Persona persona = personaService.buscarPersonaPorId(createDto.getEpe_per_id());

            // Verificar si los IDs existen
            if (empresa == null || persona == null) {
                throw new IllegalArgumentException("ID de la Empresa o Persona no encontrado.");
            }

            // Convertir DTO a entidad
            EmpresaPersona empresaPersona = EmpresaPersonaCreateDTO.toEntity(createDto, empresa, persona);

            // Crear la relacion EmpresaPersona
            EmpresaPersona nuevaEmpresaPersona = empresaPersonaService.createEmpresaPersona(empresaPersona);

            // Convertir entidad a DTO
            EmpresaPersonaReadDTO nuevaEmpresaPersonaReadDTO = EmpresaPersonaReadDTO.fromEntity(nuevaEmpresaPersona);

            // Actualizar los valores de per_es_dueno_emp y per_es_reptec_emp en Persona
            persona.setPer_es_dueno_emp(createDto.isEpe_es_dueno_emp());
            persona.setPer_es_reptec_emp(createDto.isEpe_es_reptec_emp());

            // Guardar la persona actualizada en la base de datos
            personaRepository.save(persona);

            // Mandar respuesta
            RespuestaDTO<EmpresaPersonaReadDTO> respuesta = new RespuestaDTO<>(nuevaEmpresaPersonaReadDTO, "Empresa-Persona", "Empresa-Persona creada con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear una nueva Empresa-Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear una nueva Empresa-Persona: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT SOLO PARA CAMBIAR LA EMPRESA DE UNA PERSONA Y SUS ROLES
    @PutMapping("/editarEmpresaDePersona/{id}")
    public ResponseEntity<?> actualizarEmpresaPersona(@PathVariable Integer id, @RequestBody EmpresaPersonaUpdateDTO updateDTO) {
        try {
            // Obtener ID de Empresa-Persona
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

            // Verificar si existe el ID de Empresa-Persona
            if (empresaPersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró la Empresa-Persona con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener ID de la nueva Empresa
            Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(updateDTO.getEpe_emp_id());

            // Verificar si existe el ID de la nueva Empresa
            if (nuevaEmpresa == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró la Empresa con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar los campos de Empresa-Persona
            empresaPersonaExistente.setEpe_es_dueno_emp(updateDTO.isEpe_es_dueno_emp());
            empresaPersonaExistente.setEpe_es_reptec_emp(updateDTO.isEpe_es_reptec_emp());
            empresaPersonaExistente.setEmpresa(nuevaEmpresa);

            // Actualizar los valores de epe_es_dueno_emp y epe_es_reptec_emp en la entidad Persona
            Persona personaExistente = empresaPersonaExistente.getPersona();
            personaExistente.setPer_es_dueno_emp(updateDTO.isEpe_es_dueno_emp());
            personaExistente.setPer_es_reptec_emp(updateDTO.isEpe_es_reptec_emp());

            // Actualizar Empresa-Persona
            empresaPersonaService.updateEmpresaPersona(empresaPersonaExistente);

            //Actualizar Persona
            personaService.updatePersona(personaExistente);

            //Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La empresa y los roles de la persona se han actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar la Empresa-Persona y los roles de la Persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarEmpresaPersona(@PathVariable Integer id) {
        try {
            // Obtener ID de Empresa-Persona
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

            //Verificar si existe
            if (empresaPersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                empresaPersonaService.deleteEmpresaPersonaById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Empresa Persona eliminada correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar la Empresa Persona. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //METODO PARA OBTENER TODAS LAS PERSONAS QUE PERTENECEN A UNA EMPRESA
    @GetMapping("personasPorEmpresa/{empresaId}")
    public ResponseEntity<?> obtenerEmpresaConPersonas(@PathVariable("empresaId") Integer empresaId) {
        try {
            // Obtener ID de la Empresa
            Empresa empresa = empresaService.obtenerEmpresaPorId(empresaId);

            // Verificar si existe el ID de la Empresa
            if (empresa == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("Empresa con ID " + empresaId + " no encontrada.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener las personas asociadas a la empresa
            List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresaId);

            // Mapear los datos de la empresa y las personas a un DTO
            EmpresaPersonaSimpleDTO empresaDTO = EmpresaPersonaSimpleDTO.fromEntity(empresa, personas);

            // Retornar la respuesta
            return ResponseEntity.ok(Collections.singletonMap("personasEmpresa", empresaDTO));

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la empresa con ID: " + empresaId + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }
}