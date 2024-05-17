package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
            List<Persona> personas = personaService.getAllPersona();

            if (personas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Personas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> personasDTO = new ArrayList<>();
            for (Persona persona : personas) {
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

                personasDTO.add(personaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("personas", personasDTO);

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
    public RespuestaDTO<Persona> crearPersona(@RequestBody Persona persona) {
        try {
            // Realizar validación de los datos
            if (persona.getPer_nombre().isEmpty() || persona.getPer_apellido().isEmpty() || persona.getPer_cuit().isEmpty()
                    || persona.getPer_tipodoc() == 0
                    || persona.getPer_numdoc().isEmpty()
                    || persona.getPer_telefono().isEmpty()
                    || persona.getPer_correo().isEmpty()
                    || persona.getPer_domic_legal().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de la Persona son obligatorios.");
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
            personaExistente.setPer_apellido(persona.getPer_apellido());
            personaExistente.setPer_cuit(persona.getPer_cuit());
            personaExistente.setPer_tipodoc(persona.getPer_tipodoc());
            personaExistente.setPer_numdoc(persona.getPer_numdoc());
            personaExistente.setPer_telefono(persona.getPer_telefono());
            personaExistente.setPer_correo(persona.getPer_correo());
            personaExistente.setPer_domic_legal(persona.getPer_domic_legal());
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






    @GetMapping("/empresaInmueblesPersona")
    public Map<String, Object> obtenerTodosDatosEmpresasEInmueblesPersona() {
        Map<String, Object> response = new HashMap<>();

        // Obtener todos los datos de EmpresaPersona
        List<EmpresaPersona> empresaPersonas = personaService.obtenerTodosLosDatosDeEmpresaPersona();
        response.put("empresa-Personas", empresaPersonas);

        // Obtener todos los datos de InmueblePersona
        List<InmueblePersona> inmueblePersonas = personaService.obtenerTodosLosDatosDeInmueblePersona();
        response.put("inmueble-Personas", inmueblePersonas);

        return response;
    }


    @GetMapping("/primeraEmpresaInmueblePersona")
    public ResponseEntity<Map<String, Object>> obtenerDatosCompletos() {
        Map<String, Object> response = new HashMap<>();

        // Obtener todos los datos de EmpresaPersona e InmueblePersona
        List<EmpresaPersona> empresaPersonas = empresaPersonaService.getAllEmpresaPersona();
        List<InmueblePersona> inmueblePersonas = inmueblePersonaService.getAllInmueblePersona();

        // Lista para combinar todas las relaciones
        List<Map<String, Object>> combinadas = new ArrayList<>();

        //CON ESTOS 2 FOR, LISTO LA PRIMERA PERSONA RELACIONADA CON UNA EMPRESA O INMUEBLE
        // Lista para almacenar los ID de empresas e inmuebles ya procesados
        Set<Integer> empresasProcesadas = new HashSet<>();
        Set<Integer> inmueblesProcesados = new HashSet<>();

        // Agregar datos de EmpresaPersona
        for (EmpresaPersona empresaPersona : empresaPersonas) {
            Persona persona = empresaPersona.getPersona();
            Empresa empresa = empresaPersona.getEmpresa();

            // Verificar si ya se ha procesado la empresa
            if (!empresasProcesadas.contains(empresa.getEmp_id())) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("per_id", persona.getPer_id());
                item.put("epe_id", empresaPersona.getEpe_id());
                item.putAll(mapPersona(persona)); // Agregar detalles de la persona
                item.put("empresas", mapEmpresa(empresa));
                item.put("inmuebles", null); // Iniciar como null si no hay inmuebles
                combinadas.add(item);

                // Agregar la empresa a la lista de procesadas
                empresasProcesadas.add(empresa.getEmp_id());
            }
        }

        // Agregar datos de InmueblePersona
        for (InmueblePersona inmueblePersona : inmueblePersonas) {
            Persona persona = inmueblePersona.getPersona();
            Inmueble inmueble = inmueblePersona.getInmueble();

            // Verificar si ya se ha procesado el inmueble
            if (!inmueblesProcesados.contains(inmueble.getInm_id())) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("per_id", persona.getPer_id());
                item.put("ipe_id", inmueblePersona.getIpe_id());
                item.putAll(mapPersona(persona)); // Agregar detalles de la persona
                item.put("empresas", null); // Iniciar como null si no hay empresas
                item.put("inmuebles", mapInmueble(inmueble));
                combinadas.add(item);

                // Agregar el inmueble a la lista de procesados
                inmueblesProcesados.add(inmueble.getInm_id());
            }
        }


        //CON ESTOS 2 FOR, LISTO TODAS LAS PERSONAS RELACIONADAS CON UNA EMPRESA O INMUEBLE
        // Agregar datos de EmpresaPersona
        /*for (EmpresaPersona empresaPersona : empresaPersonas) {
            Persona persona = empresaPersona.getPersona();
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("per_id", persona.getPer_id());
            item.put("epe_id", empresaPersona.getEpe_id());
            item.putAll(mapPersona(persona)); // Agregar detalles de la persona
            item.put("empresas", mapEmpresa(empresaPersona.getEmpresa()));
            item.put("inmuebles", null); // Iniciar como null si no hay inmuebles

            combinadas.add(item);
        }

        // Agregar datos de InmueblePersona
        for (InmueblePersona inmueblePersona : inmueblePersonas) {
            Persona persona = inmueblePersona.getPersona();
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("per_id", persona.getPer_id());
            item.put("ipe_id", inmueblePersona.getIpe_id());
            item.putAll(mapPersona(persona)); // Agregar detalles de la persona
            item.put("empresas", null); // Iniciar como null si no hay empresas
            item.put("inmuebles", mapInmueble(inmueblePersona.getInmueble()));

            combinadas.add(item);
        }*/

        // Ordenar combinadas por `per_id` para tener todos los datos completos pero ordenados por el ID de la persona
        combinadas.sort(Comparator.comparing(map -> (Integer) map.get("per_id")));

        response.put("empresaInmueblePersona", combinadas);

        return ResponseEntity.ok(response); // Devolver la respuesta HTTP
    }

    // Métodos para mapear datos de la persona
    private Map<String, Object> mapPersona(Persona persona) {
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
        return personaMap;
    }

    // Método para mapear datos de la empresa
    private Map<String, Object> mapEmpresa(Empresa empresa) {
        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("emp_id", empresa.getEmp_id());
        empresaMap.put("emp_razon", empresa.getEmp_razon());
        return empresaMap;
    }

    // Método para mapear datos del inmueble
    private Map<String, Object> mapInmueble(Inmueble inmueble) {
        Map<String, Object> inmuebleMap = new LinkedHashMap<>();
        inmuebleMap.put("inm_id", inmueble.getInm_id());
        inmuebleMap.put("inm_padron", inmueble.getInm_padron());
        return inmuebleMap;
    }
























    /*LISTAR PERSONAS CON TODDA SUS EMPRESAS E INMUEBLES
    @GetMapping("/empresaInmueblesPersona")
    public Map<String, Object> obtenerTodosDatosEmpresasEInmueblesPersona() {
        Map<String, Object> response = new HashMap<>();

        // Obtener todos los datos de EmpresaPersona
        List<EmpresaPersona> empresaPersonas = personaService.obtenerTodosLosDatosDeEmpresaPersona();
        response.put("empresa-Personas", empresaPersonas);

        // Obtener todos los datos de InmueblePersona
        List<InmueblePersona> inmueblePersonas = personaService.obtenerTodosLosDatosDeInmueblePersona();
        response.put("inmueble-Personas", inmueblePersonas);

        return response;
    }

    @GetMapping("/primeraEmpresasInmueblePersona")
    public Map<String, Object> obtenerPrimerDatoEmpresasEInmueblesPersona() {
        Map<String, Object> response = new HashMap<>();

        // Obtener todos los datos de EmpresaPersona
        List<EmpresaPersona> empresaPersonas = personaService.obtenerPrimeroLosDatosDeEmpresaPersona();

        // Obtener todos los datos de InmueblePersona
        List<InmueblePersona> inmueblePersonas = personaService.obtenerPrimeroLosDatosDeInmueblePersona();

        // Mapear los datos por persona
        Map<Integer, Map<String, Object>> personasMap = new HashMap<>();

        // Agregar empresas a personasMap
        for (EmpresaPersona empresaPersona : empresaPersonas) {
            Persona persona = empresaPersona.getPersona();
            int perId = persona.getPer_id();
            if (!personasMap.containsKey(perId)) {
                Map<String, Object> personaMap = mapPersona(persona);
                personaMap.put("empresas", new ArrayList<>());
                personaMap.put("inmuebles", new ArrayList<>());
                personasMap.put(perId, personaMap);
            }
            Map<String, Object> personaMap = personasMap.get(perId);
            List<Map<String, Object>> empresas = (List<Map<String, Object>>) personaMap.get("empresas");
            Map<String, Object> empresaMap = mapEmpresa(empresaPersona.getEmpresa());
            empresas.add(empresaMap);
        }

        // Agregar inmuebles a personasMap
        for (InmueblePersona inmueblePersona : inmueblePersonas) {
            Persona persona = inmueblePersona.getPersona();
            int perId = persona.getPer_id();
            if (!personasMap.containsKey(perId)) {
                Map<String, Object> personaMap = mapPersona(persona);
                personaMap.put("empresas", new ArrayList<>());
                personaMap.put("inmuebles", new ArrayList<>());
                personasMap.put(perId, personaMap);
            }
            Map<String, Object> personaMap = personasMap.get(perId);
            List<Map<String, Object>> inmuebles = (List<Map<String, Object>>) personaMap.get("inmuebles");
            Map<String, Object> inmuebleMap = mapInmueble(inmueblePersona.getInmueble());
            inmuebles.add(inmuebleMap);
        }

        // Convertir personasMap a List<Map<String, Object>>
        List<Map<String, Object>> personasDTO = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : personasMap.entrySet()) {
            personasDTO.add(entry.getValue());
        }

        // Agregar la lista completa al objeto de respuesta
        response.put("empresaInmueblePersona", personasDTO);

        return response;
    }


    // Método para mapear los datos de la persona a un DTO
    private Map<String, Object> mapPersona(Persona persona) {
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
        return personaMap;
    }

    // Método para mapear los datos de la empresa a un DTO
    private Map<String, Object> mapEmpresa(Empresa empresa) {
        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("emp_id", empresa.getEmp_id());
        empresaMap.put("emp_razon", empresa.getEmp_razon());
        // Agregar otros campos de empresa si es necesario
        return empresaMap;
    }

    // Método para mapear los datos del inmueble a un DTO
    private Map<String, Object> mapInmueble(Inmueble inmueble) {
        Map<String, Object> inmuebleMap = new LinkedHashMap<>();
        inmuebleMap.put("inm_id", inmueble.getInm_id());
        inmuebleMap.put("inm_padron", inmueble.getInm_padron());
        // Agregar otros campos de inmueble si es necesario
        return inmuebleMap;
    }*/
}
