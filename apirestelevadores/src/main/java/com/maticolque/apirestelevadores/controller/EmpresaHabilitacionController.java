package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaHabilitacionService;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.EmpresaService;
import com.maticolque.apirestelevadores.service.RevisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/v1/empresaHabilitacion")
public class EmpresaHabilitacionController {

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private RevisorService revisorService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de Empresa-Habilitacion
            List<EmpresaHabilitacion> empresasHabilitacion = empresaHabilitacionService.getAllEmpresaHabilitacion();

            // Verificar la lista de Empresa-Habilitacion
            if (empresasHabilitacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresa-Habilitacion.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<EmpresaHabilitacionReadDTO> empresaHabilitacionReadDTO = new ArrayList<>();
            for (EmpresaHabilitacion empresaHabilitacion : empresasHabilitacion) {
                EmpresaHabilitacionReadDTO dto = EmpresaHabilitacionReadDTO.fromEntity(empresaHabilitacion);
                empresaHabilitacionReadDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionEmpresas", empresaHabilitacionReadDTO); //Agregar la lista

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresa-Habilitacion: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaHabilitacionPorId(@PathVariable Integer id) {
        try {
            EmpresaHabilitacion empresaHabilitacionExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);

            if (empresaHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(empresaHabilitacionExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Empresa de Habilitacion. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearEmpresaHabilitacion(@RequestBody EmpresaHabilitacionCreateDTO createDTO) {
        try {
            // Validar datos
            if (createDTO.getEha_fecha() == null
                    || createDTO.getEha_expediente().isEmpty()
                    || createDTO.getEha_vto_hab() == null) {
                throw new IllegalArgumentException("Todos los datos de Empresa de Habilitacion son obligatorios.");
            }
            if (createDTO.getEha_emp_id() == 0) {
                throw new IllegalArgumentException("La Empresa es obligatoria.");
            }
            if (createDTO.getEha_rev_id() == 0) {
                throw new IllegalArgumentException("El Revisor es obligatorio.");
            }

            // Buscar Empresa y Revisor por sus IDs
            Empresa empresa = empresaService.buscarEmpresaPorId(createDTO.getEha_emp_id());
            Revisor revisor = revisorService.buscarRevisorPorId(createDTO.getEha_rev_id());

            // Verificar si los IDs existen
            if (empresa == null || revisor == null) {
                throw new IllegalArgumentException("ID de Empresa o Revisor no encontrado.");
            }

            // Verificar si la empresa tiene al menos una persona asociada
            boolean tienePersonas = empresaPersonaService.verificarRelacionesConPersonas(createDTO.getEha_emp_id());
            if (!tienePersonas) {
                throw new IllegalArgumentException("La Empresa debe tener al menos una persona asociada antes de ser habilitada.");
            }

            // Verificar si la empresa ya tiene un revisor asignado
            boolean tieneRevisor = empresaHabilitacionService.empresaTieneRevisorAsignado(createDTO.getEha_emp_id());
            if (tieneRevisor) {
                throw new IllegalArgumentException("Esta Empresa ya tiene un Revisor asignado. Por favor seleccione otra Empresa.");
            }

            // Convertir DTO a entidad
            EmpresaHabilitacion empresaHabilitacion = EmpresaHabilitacionCreateDTO.toEntity(createDTO, empresa, revisor);

            // Crear Empresa-Habilitacion
            EmpresaHabilitacion nuevaEmpresaHabilitacion = empresaHabilitacionService.createEmpresaHabilitacion(empresaHabilitacion);

            // Convertir entidad a DTO
            EmpresaHabilitacionReadDTO nuevaEmpresaHabilitacionDTO = EmpresaHabilitacionReadDTO.fromEntity(nuevaEmpresaHabilitacion);

            // Mandar respuesta
            RespuestaDTO<EmpresaHabilitacionReadDTO> respuesta = new RespuestaDTO<>(nuevaEmpresaHabilitacionDTO, "Empresa-Habilitacion", "Empresa-Habilitacion creada con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear una nueva Empresa-Habilitacion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear una nueva Empresa-Habilitacion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT EDITAR CON ID
    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarDatosEmpresaHabilitacion(@PathVariable Integer id, @RequestBody EmpresaHabilitacionUpdateDTO updateDTO) {
        try {
            // Obtener ID de Empresa-Habilitacion
            EmpresaHabilitacion empresaHabilitacionExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);

            // Verificar si existe el ID de Empresa-Habilitacion
            if (empresaHabilitacionExistente == null) {
                // Manejar caso de entidad no encontrada
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la Empresa-Habilitación con el ID proporcionado.");
            }

            // Validar los datos del DTO
            if (updateDTO.getEha_fecha().isEmpty() || updateDTO.getEha_expediente().isEmpty() || updateDTO.getEha_vto_hab().isEmpty()){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos de la Empresa-Habilitación son obligatorios..")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Obtener ID del nuevo Revisor
            Revisor nuevoRevisor = revisorService.buscarRevisorPorId(updateDTO.getEha_rev_id());

            // Verificar si existe el ID del nuevo Revisor
            if (nuevoRevisor == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Revisor con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir String a LocalDate para fechas en DTO
            LocalDate fecha = LocalDate.parse(updateDTO.getEha_fecha());
            LocalDate vtoHab = LocalDate.parse(updateDTO.getEha_vto_hab());

            // Actualizar los campos de Empresa-Habilitación
            empresaHabilitacionExistente.setEha_fecha(fecha);
            empresaHabilitacionExistente.setEha_expediente(updateDTO.getEha_expediente());
            empresaHabilitacionExistente.setEha_habilitada(updateDTO.isEha_habilitada());
            empresaHabilitacionExistente.setEha_vto_hab(vtoHab);
            empresaHabilitacionExistente.setEha_activo(updateDTO.isEha_activo());

            // Actualizar el Revisor si se proporcionó un nuevo ID
            empresaHabilitacionExistente.setRevisor(nuevoRevisor);

            //Actualizar Empresa-Habilitación
            empresaHabilitacionService.updateEmpresaHabilitacion(empresaHabilitacionExistente);

            //Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar la Empresa-Habilitación: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarEmpresaHabilitacion(@PathVariable Integer id) {
        try {
            EmpresaHabilitacion empresaHabilitacionExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);

            if (empresaHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                empresaHabilitacionService.deleteEmpresaHabilitacionById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Empresa de Habilitacion eliminada correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar la Empresa de Habilitacion. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}