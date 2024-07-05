package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private MedioElevacionService medioElevacionService;

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Empresa> empresas = empresaService.getAllEmpresa();

            if (empresas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> empresasDTO = new ArrayList<>();
            for (Empresa empresa : empresas) {
                Map<String, Object> empresaMap = new LinkedHashMap<>();

                empresaMap.put("emp_id", empresa.getEmp_id());
                empresaMap.put("emp_razon", empresa.getEmp_razon());
                empresaMap.put("emp_cuit", empresa.getEmp_cuit());
                empresaMap.put("emp_domic_legal", empresa.getEmp_domic_legal());
                empresaMap.put("emp_telefono", empresa.getEmp_telefono());
                empresaMap.put("emp_correo", empresa.getEmp_correo());
                empresaMap.put("emp_activa", empresa.isEmp_activa());

                empresasDTO.add(empresaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);

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

    //LISTAR CON PARAMETROS
    //GET
    /*@GetMapping
    public ResponseEntity<?> listarTodo(@RequestParam(value = "activos", required = false) Boolean activos) {
        try {
            List<Empresa> empresas;
            if (activos == null) {
                empresas = empresaService.getAllEmpresa();
            } else {
                empresas = empresaService.getEmpresasByActiva(activos);
            }

            if (empresas.isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontraron Empresas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> empresasDTO = new ArrayList<>();
            for (Empresa empresa : empresas) {
                Map<String, Object> empresaMap = new LinkedHashMap<>();

                empresaMap.put("emp_id", empresa.getEmp_id());
                empresaMap.put("emp_razon", empresa.getEmp_razon());
                empresaMap.put("emp_cuit", empresa.getEmp_cuit());
                empresaMap.put("emp_domic_legal", empresa.getEmp_domic_legal());
                empresaMap.put("emp_telefono", empresa.getEmp_telefono());
                empresaMap.put("emp_correo", empresa.getEmp_correo());
                empresaMap.put("emp_activa", empresa.isActiva());

                empresasDTO.add(empresaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresas: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }*/

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
    public RespuestaDTO<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
        try {
            // Realizar validación de los datos
            if (empresa.getEmp_cuit().isEmpty() || empresa.getEmp_telefono().isEmpty() || empresa.getEmp_razon().isEmpty() ||
                    empresa.getEmp_domic_legal().isEmpty() || empresa.getEmp_correo().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de la Empresa son obligatorios.");
            }

            // Llamar al servicio para crear la Empresa
            Empresa nuevoEmpresa = empresaService.createEmpresa(empresa);
            return new RespuestaDTO<>(nuevoEmpresa, "Empresa creado con éxito");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa: " + e.getMessage());
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarEmpresa(@PathVariable Integer id, @RequestBody Empresa empresa) {
        try {
            // Lógica para modificar la Empresa
            Empresa empresaExistente = empresaService.buscarEmpresaPorId(id);

            if (empresaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            empresaExistente.setEmp_razon(empresa.getEmp_razon());
            empresaExistente.setEmp_domic_legal(empresa.getEmp_domic_legal());
            empresaExistente.setEmp_cuit(empresa.getEmp_cuit());
            empresaExistente.setEmp_telefono(empresa.getEmp_telefono());
            empresaExistente.setEmp_correo(empresa.getEmp_correo());
            empresaExistente.setEmp_activa(empresa.isEmp_activa());

            empresaService.updateEmpresa(empresaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Empresa."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Integer id) {
        try {

            String resultado = empresaService.eliminarEmpresaSiNoTieneRelaciones(id);

            if (resultado.equals("Empresa eliminada correctamente.")) {
                return ResponseEntity.ok().body(ErrorDTO.builder().code("200 OK").message(resultado).build());
            } else if (resultado.equals("El ID proporcionado de la Empresa no existe.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder().code("404 NOT FOUND").message(resultado).build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().code("400 BAD REQUEST").message(resultado).build());
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al eliminar la empresa: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }
}
