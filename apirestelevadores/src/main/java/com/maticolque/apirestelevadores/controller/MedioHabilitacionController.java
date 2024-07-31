package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/v1/medioHabilitacion")
public class MedioHabilitacionController {

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;

    @Autowired
    private MedioElevacionService medioElevacionService;

    @Autowired
    private TipoMaquinaService tipoMaquinaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private RevisorService revisorService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            // Obtener la lista de Medio-Habilitacion
            List<MedioHabilitacion> medioHabilitaciones = medioHabilitacionService.getAllMedioHabilitacion();

            // Verificar la lista de Medio-Habilitacion
            if (medioHabilitaciones.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medio-Habilitacion.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<MedioHabilitacionReadDTO> medioHabilitacionReadDTO = new ArrayList<>();
            for (MedioHabilitacion medioHabilitacion : medioHabilitaciones) {
                MedioHabilitacionReadDTO dto = MedioHabilitacionReadDTO.fromEntity(medioHabilitacion);
                medioHabilitacionReadDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionMDE", medioHabilitacionReadDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medio-Habilitacion: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioHabilitacionPorId(@PathVariable Integer id) {
        try {
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            if (medioHabilitacionExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                return ResponseEntity.ok(medioHabilitacionExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Medio de Habilitación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearMedioHabilitacion(@RequestBody MedioHabilitacionCreateDTO dto) {
        try {
            // Validar datos
            if (dto.getMha_mde_id() == 0) {
                throw new IllegalArgumentException("El Medio de Elevación es obligatorio.");
            }

            // Buscar ID del Medio de Elevación
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(dto.getMha_mde_id());
            // Verificar si el ID existe
            if (medioElevacion == null) {
                throw new IllegalArgumentException("El Medio de Elevación con el ID proporcionado no existe.");
            }

            // Buscar ID de la Empresa asociada si existe, si no existe agregar otra Empresa
            Empresa empresa = null;
            if (medioElevacion.getEmpresa() == null) {
                // Si el Medio de Elevación no tiene una empresa asociada, validar el ID de la empresa en el DTO
                if (dto.getMha_emp_id() == 0) {
                    throw new IllegalArgumentException("La Empresa es obligatoria cuando el Medio de Elevación no tiene una empresa asociada.");
                }

                // Buscar la Empresa
                empresa = empresaService.buscarEmpresaPorId(dto.getMha_emp_id());
                if (empresa == null) {
                    throw new IllegalArgumentException("La Empresa con el ID proporcionado no existe.");
                }
            } else if (dto.getMha_emp_id() != 0) {
                // Si el Medio de Elevación ya tiene una empresa asociada, validar que el ID de empresa en el DTO sea 0
                if (dto.getMha_emp_id() != 0) {
                    throw new IllegalArgumentException("Este Medio de Elevación ya tiene una empresa asociada. Envíe el ID de la Empresa como 0.");
                }
                // Usar la empresa asociada al Medio de Elevación
                empresa = medioElevacion.getEmpresa();
            } else {
                // Si el Medio de Elevación tiene una empresa asociada y el ID en el DTO es 0, asignar la empresa asociada
                empresa = medioElevacion.getEmpresa();
            }

            // Buscar ID de la Persona
            Persona persona = personaService.buscarPersonaPorId(dto.getMha_per_id());
            // Verificar si el ID existe
            if (persona == null) {
                throw new IllegalArgumentException("La Persona con el ID proporcionado no existe.");
            }

            // Buscar ID del Revisor
            Revisor revisor = revisorService.buscarRevisorPorId(dto.getMha_rev_id());
            // Verificar si el ID existe
            if (revisor == null) {
                throw new IllegalArgumentException("El Revisor con el ID proporcionado no existe.");
            }

            // Convertir DTO a entidad
            MedioHabilitacion medioHabilitacion = MedioHabilitacionCreateDTO.toEntity(dto, medioElevacion, empresa, persona, revisor);

            // Crear Medio-Habilitacion
            MedioHabilitacion nuevoMedioHabilitacion = medioHabilitacionService.createMedioHabilitacion(medioHabilitacion);

            // Convertir entidad a DTO
            MedioHabilitacionReadDTO nuevoMedioHabilitacionReadDTO = MedioHabilitacionReadDTO.fromEntity(nuevoMedioHabilitacion);

            // Mandar respuesta
            RespuestaDTO<MedioHabilitacionReadDTO> respuesta = new RespuestaDTO<>(nuevoMedioHabilitacionReadDTO, "Medio-Habilitacion", "Medio-Habilitacion creada con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un Medio-Habilitacion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un Medio-Habilitacion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //PUT EDITAR CON ID
    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarMedioHabilitacion(@PathVariable Integer id, @RequestBody MedioHabilitacionUpdateDTO updateDTO) {
        try {
            // Obtener ID de Medio-Habilitacion
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            // Verificar si existe el ID de Medio-Habilitacion
            if (medioHabilitacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el Medio-Habilitacion con el ID proporcionado.");
            }

            /*
            // Obtener y verificar el Medio de Elevación
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(updateDTO.getMha_mde_id());
            if (medioElevacion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Medio de Elevación con el ID proporcionado no existe.");
            }

            // Verificar si el Medio de Elevación ya tiene una empresa asociada
            Empresa empresaExistente = medioElevacion.getEmpresa();

            // Si tiene una empresa y el ID de la empresa en el DTO no es 0, devolver el mensaje de error
            if (empresaExistente != null && updateDTO.getMha_emp_id() != 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este Medio de Elevación ya tiene una empresa asociada. Envíe el ID de la Empresa como 0.");
            }

            // Si no tiene una empresa asociada, obtener y verificar la Empresa del updateDTO
            Empresa empresa = null;
            if (empresaExistente == null && updateDTO.getMha_emp_id() != 0) {
                empresa = empresaService.buscarEmpresaPorId(updateDTO.getMha_emp_id());
                if (empresa == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Empresa con el ID proporcionado no existe.");
                }
            } else if (empresaExistente != null) {
                // Si ya tiene una empresa asociada, utilizar la existente
                empresa = empresaExistente;
            }

            // Obtener y verificar la Persona
            Persona persona = personaService.buscarPersonaPorId(updateDTO.getMha_per_id());
            if (persona == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Persona con el ID proporcionado no existe.");
            }*/

            // Validar los datos del DTO
            if (updateDTO.getMha_inm_padron_guardado() == 0 || updateDTO.getMha_fecha().isEmpty() || updateDTO.getMha_expediente().isEmpty() ||
                updateDTO.getMha_fecha_vto().isEmpty() || updateDTO.getMha_fecha_pago().isEmpty() || updateDTO.getMha_fecha_inspec().isEmpty()){
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos del Medio-Habilitación son obligatorios..")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Obtener y verificar el ID del Revisor
            Revisor revisor = revisorService.buscarRevisorPorId(updateDTO.getMha_rev_id());
            if (revisor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Revisor con el ID proporcionado no existe.");
            }

            // Convertir Strings a LocalDate para las fechas en el DTO
            LocalDate fecha = LocalDate.parse(updateDTO.getMha_fecha());
            LocalDate fechaVto = LocalDate.parse(updateDTO.getMha_fecha_vto());
            LocalDate fechaPago = LocalDate.parse(updateDTO.getMha_fecha_pago());
            LocalDate fechaInspec = LocalDate.parse(updateDTO.getMha_fecha_inspec());

            // Actualizar los campos del Medio-Habilitación
            medioHabilitacionExistente.setMha_inm_padron_guardado(updateDTO.getMha_inm_padron_guardado());
            //medioHabilitacionExistente.setMedioElevacion(medioElevacion);
            //medioHabilitacionExistente.setEmpresa(empresa);
            //medioHabilitacionExistente.setPersona(persona);
            medioHabilitacionExistente.setMha_fecha(fecha);
            medioHabilitacionExistente.setMha_expediente(updateDTO.getMha_expediente());
            medioHabilitacionExistente.setMha_fecha_vto(fechaVto);
            medioHabilitacionExistente.setMha_fecha_pago(fechaPago);
            medioHabilitacionExistente.setMha_fecha_inspec(fechaInspec);
            medioHabilitacionExistente.setMha_habilitado(updateDTO.isMha_habilitado());
            medioHabilitacionExistente.setMha_oblea_entregada(updateDTO.isMha_oblea_entregada());
            medioHabilitacionExistente.setMha_activo(updateDTO.isMha_activo());
            medioHabilitacionExistente.setRevisor(revisor);

            //Actualizar Medio-Habilitación
            medioHabilitacionService.updateMedioHabilitacion(medioHabilitacionExistente);

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
                    .message("Error al modificar el Medio-Habilitacion: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarMedioHabilitacion(@PathVariable Integer id) {
        try {
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            if (medioHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                medioHabilitacionService.deleteMedioHabilitacionById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Medio de Habilitación eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Medio de Habilitación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}