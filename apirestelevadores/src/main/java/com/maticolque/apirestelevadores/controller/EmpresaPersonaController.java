package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.EmpresaService;
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
@RequestMapping("api/v1/empresaPersona")
public class EmpresaPersonaController {

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    PersonaService personaService;

    @Autowired
    private PersonaRepository personaRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarEmpresaPersonas() {
        try {
            List<EmpresaPersona> empresaPersonas = empresaPersonaService.getAllEmpresaPersona();
            List<Map<String, Object>> empresaPersonasDTO = new ArrayList<>();

            for (EmpresaPersona empresaPersona : empresaPersonas) {
                Map<String, Object> empresaPersonaMap = new LinkedHashMap<>();
                empresaPersonaMap.put("epe_id", empresaPersona.getEpe_id());
                empresaPersonaMap.put("epe_es_dueno_emp", empresaPersona.isEpe_es_dueno_emp());
                empresaPersonaMap.put("epe_es_reptec_emp", empresaPersona.isEpe_es_reptec_emp());

                // Extraer los datos de la empresa asociada
                Empresa empresa = empresaPersona.getEmpresa();
                Map<String, Object> empresaMap = new LinkedHashMap<>();
                empresaMap.put("emp_id", empresa.getEmp_id());
                empresaMap.put("emp_razon", empresa.getEmp_razon());
                empresaMap.put("emp_cuit", empresa.getEmp_cuit());
                empresaMap.put("emp_domic_legal", empresa.getEmp_domic_legal());
                empresaMap.put("emp_telefono", empresa.getEmp_telefono());
                empresaMap.put("emp_correo", empresa.getEmp_correo());
                empresaMap.put("emp_activa", empresa.isEmp_activa());
                empresaPersonaMap.put("empresa", empresaMap);

                // Extraer los datos de la persona asociada
                Persona persona = empresaPersona.getPersona();
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
                empresaPersonaMap.put("persona", personaMap);

                empresaPersonasDTO.add(empresaPersonaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("empresaPersonas", empresaPersonasDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresa Persona: " + e.getMessage())
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
                    .message("Error al buscar la Empresa Persona." + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<EmpresaPersona> crearEmpresaPersona(@RequestBody EmpresaPersona empresaPersona) {
        try {
            // Realizar validación de los datos
            if (empresaPersona.getEmpresa().getEmp_id() == 0) {
                throw new IllegalArgumentException("La Empresa es obligatoria.");
            } else if (empresaPersona.getPersona().getPer_id() == 0) {
                throw new IllegalArgumentException("La Persona es obligatoria.");
            }

            // Llamar al servicio para crear la Empresa Persona
            EmpresaPersona nuevoEmpresaPersona = empresaPersonaService.createEmpresaPersona(empresaPersona);

            // Obtener la persona relacionada
            Persona persona = nuevoEmpresaPersona.getPersona();

            // Cargar la persona desde la base de datos para asegurarnos de tener la versión más reciente
            persona = personaRepository.findById(persona.getPer_id()).orElse(null);
            if (persona == null) {
                throw new EntityNotFoundException("No se encontró la persona con ID: " + nuevoEmpresaPersona.getPersona().getPer_id());
            }

            // Actualizar los valores de per_es_dueno_emp y per_es_reptec_emp
            persona.setPer_es_dueno_emp(empresaPersona.isEpe_es_dueno_emp());
            persona.setPer_es_reptec_emp(empresaPersona.isEpe_es_reptec_emp());

            // Guardar la persona actualizada en la base de datos
            personaRepository.save(persona);

            return new RespuestaDTO<>(nuevoEmpresaPersona, "Empresa Persona creada con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa Persona: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa Persona:  " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarEmpresaPersona(@PathVariable Integer id, @RequestBody EmpresaPersona empresaPersona) {
        try {
            // Lógica para modificar la Empresa Persona
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

            if (empresaPersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            empresaPersonaExistente.setEpe_es_dueno_emp(empresaPersona.isEpe_es_dueno_emp());
            empresaPersonaExistente.setEpe_es_reptec_emp(empresaPersona.isEpe_es_reptec_emp());
            empresaPersonaExistente.setEmpresa(empresaPersona.getEmpresa());
            empresaPersonaExistente.setPersona(empresaPersona.getPersona());

            empresaPersonaService.updateEmpresaPersona(empresaPersonaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);


        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Empresa Persona."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //PUT SOLO PARA CAMBIAR LA EMPRESA DE UNA PERSONA
    @PutMapping("/editarEmpresaDePersona/{id}")
    public ResponseEntity<?> actualizarEmpresaDePersona(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Obtener el ID de la nueva empresa
            Integer nuevaEmpresaId = (Integer) requestBody.get("emp_id");

            // Obtener los valores de epe_es_dueno_emp y epe_es_reptec_emp
            boolean esDueno = (boolean) requestBody.getOrDefault("epe_es_dueno_emp", false);
            boolean esReptec = (boolean) requestBody.getOrDefault("epe_es_reptec_emp", false);

            // Verificar si la relación EmpresaPersona existe para la persona específica
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);
            if (empresaPersonaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró una relación Empresa-Persona para la persona con ID: " + id)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener la nueva empresa
            Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(nuevaEmpresaId);
            if (nuevaEmpresa == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró la empresa con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar la empresa de la persona en la relación EmpresaPersona
            empresaPersonaExistente.setEmpresa(nuevaEmpresa);

            // Actualizar los valores de epe_es_dueno_emp y epe_es_reptec_emp en la relación EmpresaPersona
            empresaPersonaExistente.setEpe_es_dueno_emp(esDueno);
            empresaPersonaExistente.setEpe_es_reptec_emp(esReptec);

            // Actualizar los valores de epe_es_dueno_emp y epe_es_reptec_emp en la entidad Persona
            Persona personaExistente = empresaPersonaExistente.getPersona();
            personaExistente.setPer_es_dueno_emp(esDueno);
            personaExistente.setPer_es_reptec_emp(esReptec);

            // Actualizar la relación EmpresaPersona y la entidad Persona en la base de datos
            empresaPersonaService.updateEmpresaPersona(empresaPersonaExistente);
            personaService.updatePersona(personaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La empresa y los roles de la persona se han actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar la empresa y los roles de la persona: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }









    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarEmpresaPersona(@PathVariable Integer id) {
        try {
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

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





    @GetMapping("personasPorEmpresa/{empresaId}")
    public ResponseEntity<?> obtenerEmpresaConPersonas(@PathVariable("empresaId") Integer empresaId) {
        try {
            // Obtener la empresa por su ID
            Empresa empresa = empresaService.obtenerEmpresaPorId(empresaId);

            // Verificar si la empresa existe
            if (empresa == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("ERR_EMPRESA_NOT_FOUND")
                        .message("Empresa con ID " + empresaId + " no encontrada.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener las personas asociadas a la empresa
            List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresaId);

            // Mapear los datos de la empresa y las personas a un DTO
            Map<String, Object> empresaDTO = mapEmpresa(empresa, personas);

            // Crear la respuesta
            //return ResponseEntity.ok(empresaDTO);
            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("personasEmpresa", empresaDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la empresa con ID: " + empresaId + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    // Método para mapear los datos de la empresa y las personas a un DTO
    private Map<String, Object> mapEmpresa(Empresa empresa, List<Persona> personas) {
        if (empresa == null) {
            return null;
        }

        Map<String, Object> empresaDTO = new LinkedHashMap<>();
        empresaDTO.put("emp_id", empresa.getEmp_id());
        empresaDTO.put("emp_razon", empresa.getEmp_razon());

        // Mapear los datos de las personas a un DTO
        List<Map<String, Object>> personasDTOList = new ArrayList<>();
        for (Persona persona : personas) {
            Map<String, Object> personaDTO = mapPersona(persona);
            personasDTOList.add(personaDTO);
        }
        empresaDTO.put("personas", personasDTOList);

        return empresaDTO;

    }

    // Método para mapear los datos de la persona a un DTO (puedes reutilizar el existente)
    private Map<String, Object> mapPersona(Persona persona) {
        if (persona == null) {
            return null;
        }

        Map<String, Object> personaDTO = new LinkedHashMap<>();
        personaDTO.put("per_id", persona.getPer_id());
        personaDTO.put("per_nombre", persona.getPer_nombre());
        // Añadir más campos según tus necesidades

        return personaDTO;
    }
















}
