package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/empresaPersona")
public class EmpresaPersonaController {

    @Autowired
    private EmpresaPersonaService empresaPersonaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<EmpresaPersona> empresaPersonas = empresaPersonaService.getAllEmpresaPersona();

            if (empresaPersonas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresa Persona.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(empresaPersonas);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
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
                throw new IllegalArgumentException("La empresa es obligatoria.");
            } else if (empresaPersona.getPersona().getPer_id() == 0) {
                throw new IllegalArgumentException("La persona es obligatoria.");
            }

            // Llamar al servicio para crear el destino
            EmpresaPersona nuevoEmpresaPersona = empresaPersonaService.createEmpresaPersona(empresaPersona);
            return new RespuestaDTO<>(nuevoEmpresaPersona, "Empresa Persona creado con éxito.");

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
            // Lógica para modificar el medio de elevación
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
}
