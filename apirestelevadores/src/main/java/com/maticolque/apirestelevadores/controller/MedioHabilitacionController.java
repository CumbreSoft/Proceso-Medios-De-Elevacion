package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
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
    private RevisorService revisorService;


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
                mediosHabilitacionMap.put("mha_inm_padron_guardado", medioHabilitacion.getMha_inm_padron_guardado());

                // Verificar si la empresa no es nula antes de agregarla al DTO y mapear solo los campos necesarios
                if (medioHabilitacion.getMedioElevacion() != null) {
                    Map<String, Object> mdeMap = new LinkedHashMap<>();
                    mdeMap.put("mde_id", medioHabilitacion.getMedioElevacion().getMde_id());
                    mdeMap.put("mde_ubicacion", medioHabilitacion.getMedioElevacion().getMde_ubicacion());
                    //mdeMap.put("tma_id", medioHabilitacion.getMedioElevacion().getTiposMaquinas().getTma_id());
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
            if(medioHabilitacion.getMha_inm_padron_guardado() == 0){
                throw new IllegalArgumentException("El Padrón es obligatorio.");
            }
            else if(medioHabilitacion.getMedioElevacion().getMde_id() == 0){
                throw new IllegalArgumentException("El Medio de Elevación es obligatorio.");
            }
            else if(medioHabilitacion.getEmpresa().getEmp_id() == 0){
                throw new IllegalArgumentException("La Empresa es obligatoria.");
            }
            else if(medioHabilitacion.getPersona().getPer_id() == 0){
                throw new IllegalArgumentException("La Persona es obligatoria.");

            }
            else if (medioHabilitacion.getMha_fecha() == null  || medioHabilitacion.getMha_expediente().isEmpty()
                    || medioHabilitacion.getMha_fecha_vto() == null
                    || medioHabilitacion.getMha_fecha_pago() == null
                    || medioHabilitacion.getMha_fecha_inspec() == null
                  /*|| medioHabilitacion.getMha_vto_hab() == null*/) {

                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");

            }
            else if(medioHabilitacion.getRevisor().getRev_id() == 0){
                throw new IllegalArgumentException("El Revisor es obligatorio.");
            }
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


    /*
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
    }*/



    //PUT EDITAR CON ID
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarMedioHabilitacion(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Lógica para modificar el Medio Habilitación
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            if (medioHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El Medio de Elevación-Habilitación con el ID proporcionado no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Actualizar Datos

            //Actulizar PADRON
            if (requestBody.containsKey("mha_inm_padron_guardado")) {
                medioHabilitacionExistente.setMha_inm_padron_guardado((int) requestBody.get("mha_inm_padron_guardado"));
            }

            //Actualizar ID MDE
            Integer nuevoMDEId = (Integer) requestBody.get("mde_id");
            if (nuevoMDEId != null) {
                MedioElevacion nuevoMDE = medioElevacionService.buscarMedioElevacionPorId(nuevoMDEId);
                if (nuevoMDE == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Medio de Elevación con el ID proporcionado no existe.");
                }
                medioHabilitacionExistente.setMedioElevacion(nuevoMDE);
            }

            /*Actualizar ID Tipo Máquina
            Integer nuevoTipoMId = (Integer) requestBody.get("tma_id");
            if (nuevoTipoMId != null) {
                TipoMaquina nuevoTipoMaquina = tipoMaquinaService.buscartipoMaquinaPorId(nuevoTipoMId);
                if (nuevoTipoMaquina == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Tipo de Máquina con el ID proporcionado no existe.");
                }
                medioHabilitacionExistente.getMedioElevacion().setTiposMaquinas(nuevoTipoMaquina);
            }*/

            //Actualizar ID Empresa
            Integer nuevaEmpresaId = (Integer) requestBody.get("emp_id");
            if (nuevaEmpresaId != null) {
                Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(nuevaEmpresaId);
                if (nuevaEmpresa == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La empresa con el ID proporcionado no existe.");
                }
                medioHabilitacionExistente.setEmpresa(nuevaEmpresa);
            }

            //Actualizar ID Persona/Técnico
            Integer nuevoTecnicoId = (Integer) requestBody.get("per_id");
            if (nuevoTecnicoId != null) {
                Persona nuevaPersona = personaService.buscarPersonaPorId(nuevoTecnicoId);
                if (nuevaPersona == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Persona con el ID proporcionado no existe.");
                }
                medioHabilitacionExistente.setPersona(nuevaPersona);
            }

            //Actulizar Fecha Habilitación
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (requestBody.containsKey("mha_fecha")) {
                try {
                    Date mha_fecha = dateFormat.parse((String) requestBody.get("mha_fecha"));
                    medioHabilitacionExistente.setMha_fecha(mha_fecha);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }

            //Actulizar Expediente Sayges
            if (requestBody.containsKey("mha_expediente")) {
                medioHabilitacionExistente.setMha_expediente((String) requestBody.get("mha_expediente"));
            }

            //Actulizar Fecha Vencimiento
            if (requestBody.containsKey("mha_fecha_vto")) {
                try {
                    Date mha_fecha_vto = dateFormat.parse((String) requestBody.get("mha_fecha_vto"));
                    medioHabilitacionExistente.setMha_fecha_vto(mha_fecha_vto);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }

            //Actulizar Fecha Pago
            if (requestBody.containsKey("mha_fecha_pago")) {
                try {
                    Date mha_fecha_pago = dateFormat.parse((String) requestBody.get("mha_fecha_pago"));
                    medioHabilitacionExistente.setMha_fecha_pago(mha_fecha_pago);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }

            //Actulizar Fecha Inspección
            if (requestBody.containsKey("mha_fecha_inspec")) {
                try {
                    Date mha_fecha_inspec = dateFormat.parse((String) requestBody.get("mha_fecha_inspec"));
                    medioHabilitacionExistente.setMha_fecha_inspec(mha_fecha_inspec);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }

            //Actulizar Medio Habilitado
            if (requestBody.containsKey("mha_habilitado")) {
                medioHabilitacionExistente.setMha_habilitado((Boolean) requestBody.get("mha_habilitado"));
            }

            //Actulizar Oblea
            if (requestBody.containsKey("mha_oblea_entregada")) {
                medioHabilitacionExistente.setMha_oblea_entregada((Boolean) requestBody.get("mha_oblea_entregada"));
            }

            //Actualizar ID Revisor
            Integer nuevoRevisorId = (Integer) requestBody.get("rev_id");
            if (nuevoRevisorId != null) {
                Revisor nuevoRevisor = revisorService.buscarRevisorPorId(nuevoRevisorId);
                if (nuevoRevisor == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Revisor con el ID proporcionado no existe.");
                }
                medioHabilitacionExistente.setRevisor(nuevoRevisor);
            }

            // Guardar los cambios
            medioHabilitacionService.updateMedioHabilitacion(medioHabilitacionExistente);

            return ResponseEntity.status(HttpStatus.OK).body("La Empresa Habilitación se actualizó correctamente.");

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
