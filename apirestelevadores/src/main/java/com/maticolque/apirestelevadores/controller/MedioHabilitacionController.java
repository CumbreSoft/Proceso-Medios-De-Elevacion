package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioHabilitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/medioHabilitacion")
public class MedioHabilitacionController {

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<MedioHabilitacion> medioHabilitaciones = medioHabilitacionService.getAllMedioHabilitacion();

            if (medioHabilitaciones.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medios de Habilitación.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(medioHabilitaciones);

            List<Map<String, Object>> habilitacionMDEDTO = new ArrayList<>();
            for (MedioHabilitacion medioHabilitacion : medioHabilitaciones) {
                Map<String, Object> mediosHabilitacionMap = new LinkedHashMap<>();

                mediosHabilitacionMap.put("mha_id", medioHabilitacion.getMha_id());

                // Verificar si la empresa no es nula antes de agregarla al DTO y mapear solo los campos necesarios
                if (medioHabilitacion.getMedioElevacion() != null) {
                    Map<String, Object> mdeMap = new LinkedHashMap<>();
                    mdeMap.put("mde_id", medioHabilitacion.getMedioElevacion().getMde_id());
                    mdeMap.put("mde_ubicacion", medioHabilitacion.getMedioElevacion().getMde_ubicacion());
                    mdeMap.put("tma_id", medioHabilitacion.getMedioElevacion().getTiposMaquinas().getTma_id());
                    mdeMap.put("tma_detalle", medioHabilitacion.getMedioElevacion().getTiposMaquinas().getTma_detalle());


                    mediosHabilitacionMap.put("medioElevacion", mdeMap);
                } else {
                    mediosHabilitacionMap.put("medioElevacion", null);
                }

                // Verificar si la empresa no es nula antes de agregarla al DTO y mapear solo los campos necesarios
                if (medioHabilitacion.getEmpresa() != null) {
                    Map<String, Object> empresaMap = new LinkedHashMap<>();
                    empresaMap.put("emp_id", medioHabilitacion.getEmpresa().getEmp_id());
                    empresaMap.put("emp_razon", medioHabilitacion.getEmpresa().getEmp_razon());
                    mediosHabilitacionMap.put("empresa", empresaMap);
                } else {
                    mediosHabilitacionMap.put("empresa", null);
                }

                // Verificar si la persona no es nula antes de agregarla al DTO y mapear solo los campos necesarios
                if (medioHabilitacion.getPersona() != null) {
                    Map<String, Object> personaMap = new LinkedHashMap<>();
                    personaMap.put("per_id", medioHabilitacion.getPersona().getPer_id());
                    personaMap.put("per_apellido", medioHabilitacion.getPersona().getPer_apellido());
                    mediosHabilitacionMap.put("persona", personaMap);
                } else {
                    mediosHabilitacionMap.put("persona", null);
                }

                mediosHabilitacionMap.put("mha_fecha", medioHabilitacion.getMha_fecha());
                mediosHabilitacionMap.put("mha_expediente", medioHabilitacion.getMha_expediente());
                mediosHabilitacionMap.put("mha_fecha_vto", medioHabilitacion.getMha_fecha_vto());
                mediosHabilitacionMap.put("mha_fecha_pago", medioHabilitacion.getMha_fecha_pago());
                mediosHabilitacionMap.put("mha_fecha_inspec", medioHabilitacion.getMha_fecha_inspec());
                mediosHabilitacionMap.put("mha_habilitado", medioHabilitacion.isMha_habilitado());
                mediosHabilitacionMap.put("mha_oblea_entregada", medioHabilitacion.isMha_oblea_entregada());

                // Verificar si el revisor no es nulo antes de agregarla al DTO y mapear solo los campos necesarios
                if (medioHabilitacion.getRevisor() != null) {
                    Map<String, Object> revisorMap = new LinkedHashMap<>();
                    revisorMap.put("rev_id", medioHabilitacion.getRevisor().getRev_id());
                    revisorMap.put("rev_apellido", medioHabilitacion.getRevisor().getRev_apellido());
                    mediosHabilitacionMap.put("revisor", revisorMap);
                } else {
                    mediosHabilitacionMap.put("revisor", null);
                }

                mediosHabilitacionMap.put("mha_activo", medioHabilitacion.isMha_activo());

                habilitacionMDEDTO.add(mediosHabilitacionMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionMDE", habilitacionMDEDTO);

            return ResponseEntity.ok(response);



        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios de Habilitación: " + e.getMessage())
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
    public RespuestaDTO<MedioHabilitacion> crearMedioHabilitacion(@RequestBody MedioHabilitacion medioHabilitacion) {
        try {
            // Realizar validación de los datos
            if (medioHabilitacion.getMha_fecha() == null  || medioHabilitacion.getMha_expediente().isEmpty()
                    || medioHabilitacion.getMha_fecha_vto() == null
                    || medioHabilitacion.getMha_fecha_pago() == null
                    || medioHabilitacion.getMha_fecha_inspec() == null
                  /*|| medioHabilitacion.getMha_vto_hab() == null*/) {

                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");

            }/*else if(medioHabilitacion.getEmpresa().getEmp_id() == 0){
                throw new IllegalArgumentException("La Empresa es obligatoria.");

            }else if(medioHabilitacion.getPersona().getPer_id() == 0){
                throw new IllegalArgumentException("La Persona es obligatoria.");

            }*//*else if(medioHabilitacion.getRevisor().getRev_id() == 0){
                throw new IllegalArgumentException("El Revisor es obligatorio.");
            }*/

            // Llamar al servicio para crear el Medio de Habilitacion
            MedioHabilitacion nuevoMedioHabilitacion = medioHabilitacionService.createMedioHabilitacion(medioHabilitacion);
            return new RespuestaDTO<>(nuevoMedioHabilitacion, "Medio Habilitación creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Habilitación: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Habilitación. " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarMedioHabilitacion(@PathVariable Integer id, @RequestBody MedioHabilitacion medioHabilitacion) {
        try {
            // Lógica para modificar el Medio Habilitación
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            if (medioHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioHabilitacionExistente.setMha_fecha(medioHabilitacion.getMha_fecha());
            //medioHabilitacionExistente.setEmpresa(medioHabilitacion.getEmpresa());
            //medioHabilitacionExistente.setPersona(medioHabilitacion.getPersona());
            medioHabilitacionExistente.setMha_expediente(medioHabilitacion.getMha_expediente());
            medioHabilitacionExistente.setMha_fecha_vto(medioHabilitacion.getMha_fecha_vto());
            medioHabilitacionExistente.setMha_fecha_pago(medioHabilitacion.getMha_fecha_pago());
            medioHabilitacionExistente.setMha_fecha_inspec(medioHabilitacion.getMha_fecha_inspec());
            //medioHabilitacionExistente.setMha_planos_aprob(medioHabilitacion.isMha_planos_aprob());
            medioHabilitacionExistente.setMha_habilitado(medioHabilitacion.isMha_habilitado());
            medioHabilitacionExistente.setMha_oblea_entregada(medioHabilitacion.isMha_oblea_entregada());
            //medioHabilitacionExistente.setMha_vto_hab(medioHabilitacion.getMha_vto_hab());
            //medioHabilitacionExistente.setRevisor(medioHabilitacion.getRevisor());

            medioHabilitacionService.updateMedioHabilitacion(medioHabilitacionExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Medio de Habilitación. " + e.getMessage())
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
