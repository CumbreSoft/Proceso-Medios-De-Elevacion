package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.EmpresaDTO;
import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.RevisorDTO;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Empresas
            List<Empresa> empresas = empresaService.getAllEmpresa();

            // Verificar la lista de Empresas
            if (empresas.isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<EmpresaDTO> empresasDTO = new ArrayList<>();
            for (Empresa empresa : empresas) {
                EmpresaDTO dto = EmpresaDTO.fromEntity(empresa);
                empresasDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresas: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaPorId(@PathVariable Integer id) {
        try {
            Empresa empresaExistente = empresaService.buscarEmpresaPorId(id);

            if (empresaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(empresaExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Empresa. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        try {
            // Validar datos
            if (empresaDTO.getEmp_cuit().isEmpty() || empresaDTO.getEmp_telefono().isEmpty() || empresaDTO.getEmp_razon().isEmpty() ||
                    empresaDTO.getEmp_domic_legal().isEmpty() || empresaDTO.getEmp_correo().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de la Empresa son obligatorios.");
            }

            // Convertir DTO a entidad
            Empresa empresa = EmpresaDTO.toEntity(empresaDTO);

            // Crear Empresa
            Empresa nuevaEmpresa = empresaService.createEmpresa(empresa);

            // Convertir entidad a DTO
            EmpresaDTO nuevaEmpresaDTO = EmpresaDTO.fromEntity(nuevaEmpresa);

            // Mandar respuesta
            RespuestaDTO<EmpresaDTO> respuesta = new RespuestaDTO<>(nuevaEmpresaDTO, "Empresa", "Empresa creada con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear una nueva Empresa: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al una nueva Empresa: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarEmpresa(@PathVariable Integer id, @RequestBody EmpresaDTO empresaDTO) {
        try {
            // Buscar el Empresa por ID
            Empresa empresaExistente = empresaService.buscarEmpresaPorId(id);

            // Verificar si existe el ID del Empresa
            if (empresaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró la Empresa con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (empresaDTO.getEmp_razon().isEmpty() || empresaDTO.getEmp_domic_legal().isEmpty() || empresaDTO.getEmp_cuit().isEmpty() ||
                empresaDTO.getEmp_telefono().isEmpty() || empresaDTO.getEmp_correo().isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos de la Empresa son obligatorios.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos del Empresa
            empresaExistente.setEmp_razon(empresaDTO.getEmp_razon());
            empresaExistente.setEmp_domic_legal(empresaDTO.getEmp_domic_legal());
            empresaExistente.setEmp_cuit(empresaDTO.getEmp_cuit());
            empresaExistente.setEmp_telefono(empresaDTO.getEmp_telefono());
            empresaExistente.setEmp_correo(empresaDTO.getEmp_correo());
            empresaExistente.setEmp_activa(empresaDTO.isEmp_activa());

            // Actualizar Empresa
            empresaService.updateEmpresa(empresaExistente);

            //Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar la Empresa: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<ErrorDTO> eliminarEmpresa(@PathVariable int id) {
        return empresaService.eliminarEmpresaSiNoTieneRelaciones(id);
    }
}