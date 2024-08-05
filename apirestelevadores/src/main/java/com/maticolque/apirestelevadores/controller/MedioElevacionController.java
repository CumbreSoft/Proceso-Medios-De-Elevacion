package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaService;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import com.maticolque.apirestelevadores.service.TipoMaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("api/v1/medioElevacion")
public class MedioElevacionController {

    @Autowired
    private MedioElevacionService medioElevacionService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private TipoMaquinaService tipoMaquinaService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener la lista de MDE
            List<MedioElevacion> mediosElevacion = medioElevacionService.getAllMedioElevacion();

            // Verificar la lista de MDE
            if (mediosElevacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron MDE.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<MedioElevacionReadDTO> medioElevacionReadDTO = new ArrayList<>();
            for (MedioElevacion medioElevacion : mediosElevacion) {
                MedioElevacionReadDTO dto = MedioElevacionReadDTO.fromEntity(medioElevacion);
                medioElevacionReadDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("mediosDeElevacion", medioElevacionReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de MDE: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioElevacionPorId(@PathVariable Integer id) {
        try {

            // Buscar MedioElevacion por ID
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            // Verificar si existe el ID
            if (medioElevacionExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            MedioElevacionReadDTO medioElevacionReadDTO = MedioElevacionReadDTO.fromEntity(medioElevacionExistente);

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioElevacion", medioElevacionReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Medio de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?>  crearMedioElevacion(@RequestBody MedioElevacionCreateDTO dto) {
        try {

            // Validar datos
            if (dto.getMde_ubicacion().isEmpty() || dto.getMde_tipo().isEmpty() || dto.getMde_niveles() == 0) {
                throw new IllegalArgumentException("No se permiten datos vacíos.");
            }

            // Buscar TipoMaquina y Empresa por sus IDs
            TipoMaquina tipoMaquina = tipoMaquinaService.buscartipoMaquinaPorId(dto.getMde_tma_id());

            // Verificar si el ID de TipoMaquina existe
            if (tipoMaquina == null) {
                throw new IllegalArgumentException("ID de TipoMaquina no encontrado.");
            }

            //Buscar empresa, puede tener o no Empresa el MDE
            Empresa empresa = null;
            if (dto.getMde_emp_id() != 0) {
                empresa = empresaService.buscarEmpresaPorId(dto.getMde_emp_id());
            }

            // Convertir DTO a entidad
            MedioElevacion medioElevacion = MedioElevacionCreateDTO.toEntity(dto, tipoMaquina, empresa);

            // Crear MDE
            MedioElevacion nuevoMedioElevacion = medioElevacionService.createMedioElevacion(medioElevacion);

            // Convertir entidad a DTO
            MedioElevacionReadDTO nuevoMedioElevacionReadDTO = MedioElevacionReadDTO.fromEntity(nuevoMedioElevacion);

            // Mandar respuesta
            RespuestaDTO<MedioElevacionReadDTO> respuesta = new RespuestaDTO<>(nuevoMedioElevacionReadDTO, "MedioDeElevacion", "MedioDeElevacion creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo MDE: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo MDE: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT PARA CAMBIAR LA EMPRESA DE MANTENIMIENTO Y TIPO DE MAQUINA CON SU ID
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarMedioElevacionCompleto(@PathVariable Integer id, @RequestBody MedioElevacionUpdateDTO dto) {
        try {

            // Buscar el MDE por ID
            MedioElevacion mdeExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            // Verificar si existe el ID del MDE
            if (mdeExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el MDE con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (dto.getMde_ubicacion().isEmpty() || dto.getMde_tipo().isEmpty() || dto.getMde_niveles() == 0) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("No se permiten datos vacíos.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar propiedades básicas si están presentes
            mdeExistente.setMde_ubicacion(dto.getMde_ubicacion());
            mdeExistente.setMde_tipo(dto.getMde_tipo());
            mdeExistente.setMde_niveles(dto.getMde_niveles());
            mdeExistente.setMde_planos_aprob(dto.isMde_planos_aprob());
            mdeExistente.setMde_expte_planos(dto.getMde_expte_planos());
            mdeExistente.setMde_activo(dto.isMde_activo());

            // Verificar y actualizar Empresa si se proporciona
            if (dto.getMde_emp_id() != 0) {
                Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(dto.getMde_emp_id());
                if (nuevaEmpresa == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La empresa con el ID proporcionado no existe.");
                }
                mdeExistente.setEmpresa(nuevaEmpresa);
            } else {
                mdeExistente.setEmpresa(null);
            }

            // Verificar y actualizar Tipo de Máquina si se proporciona
            if (dto.getMde_tma_id() != 0) {
                TipoMaquina nuevoTipoMaquina = tipoMaquinaService.buscartipoMaquinaPorId(dto.getMde_tma_id());
                if (nuevoTipoMaquina == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tipo de máquina con el ID proporcionado no existe.");
                }
                mdeExistente.setTiposMaquinas(nuevoTipoMaquina);
            }

            //Actualizar MDE
            medioElevacionService.updateMedioElevacion(mdeExistente);

            // Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El MDE se actualizó correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar errores generales
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar el MDE: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<ErrorDTO> eliminarMDE(@PathVariable int id) {
        return medioElevacionService.eliminarMedioElevacionSiNoTieneRelaciones(id);
    }
}