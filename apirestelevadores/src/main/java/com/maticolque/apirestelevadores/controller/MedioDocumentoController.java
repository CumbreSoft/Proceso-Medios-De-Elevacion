package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioDocumentoService;
import com.maticolque.apirestelevadores.service.MedioHabilitacionService;
import com.maticolque.apirestelevadores.service.RevisorService;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api/v1/medioDocumento")
public class MedioDocumentoController {

    @Autowired
    private MedioDocumentoService medioDocumentoService;

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;

    @Autowired
    private RevisorService revisorService;



    //METODOS
    private Map<String, Object> mapMedioDocumento(MedioDocumento medioDocumento) {
        if (medioDocumento == null) {
            return null;
        }

        Map<String, Object> medioDocumentoMap = new LinkedHashMap<>();
        medioDocumentoMap.put("mdo_id", medioDocumento.getMdo_id());
        medioDocumentoMap.put("mdeHabilitacion", mapMedioHabilitacion(medioDocumento.getMedioHabilitacion()));
        medioDocumentoMap.put("tipoAdjunto", mapTipoAdjunto(medioDocumento.getTipoAdjunto()));
        medioDocumentoMap.put("mdo_adjunto_orden", medioDocumento.getMdo_adjunto_orden());
        medioDocumentoMap.put("mdo_adjunto_fecha", medioDocumento.getMdo_adjunto_fecha());
        medioDocumentoMap.put("revisorInterno", mapRevisorInterno(medioDocumento.getRevisor()));

        return medioDocumentoMap;
    }

    private Map<String, Object> mapMedioHabilitacion(MedioHabilitacion medioHabilitacion) {
        if (medioHabilitacion == null) {
            return null;
        }

        Map<String, Object> mdeHabilitacionMap = new LinkedHashMap<>();
        mdeHabilitacionMap.put("mha_id", medioHabilitacion.getMha_id());
        mdeHabilitacionMap.put("mha_inm_padron_guardado", medioHabilitacion.getMha_inm_padron_guardado());
        mdeHabilitacionMap.put("medioElevacion", mapMDE(medioHabilitacion.getMedioElevacion()));
        mdeHabilitacionMap.put("empresa", mapEmpresa(medioHabilitacion.getEmpresa()));
        mdeHabilitacionMap.put("persona", mapPersona(medioHabilitacion.getPersona()));

        mdeHabilitacionMap.put("mha_fecha", medioHabilitacion.getMha_fecha());
        mdeHabilitacionMap.put("mha_expediente", medioHabilitacion.getMha_expediente());
        mdeHabilitacionMap.put("mha_fecha_vto", medioHabilitacion.getMha_fecha_vto());
        mdeHabilitacionMap.put("mha_fecha_pago", medioHabilitacion.getMha_fecha_pago());
        mdeHabilitacionMap.put("mha_fecha_inspec", medioHabilitacion.getMha_fecha_inspec());
        mdeHabilitacionMap.put("mha_habilitado", medioHabilitacion.isMha_habilitado());
        mdeHabilitacionMap.put("mha_oblea_entregada", medioHabilitacion.isMha_oblea_entregada());
        mdeHabilitacionMap.put("revisor", mapRevisor(medioHabilitacion.getRevisor()));
        mdeHabilitacionMap.put("mha_activo", medioHabilitacion.isMha_activo());

        return mdeHabilitacionMap;

    }

    //MDE
    private Map<String, Object> mapMDE(MedioElevacion medioElevacion) {
        if (medioElevacion == null) {
            return null;
        }

        Map<String, Object> mdeMap = new LinkedHashMap<>();
        mdeMap.put("mde_id", medioElevacion.getMde_id());
        mdeMap.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
        mdeMap.put("tma_detalle", medioElevacion.getTiposMaquinas().getTma_detalle());
        return mdeMap;
    }

    //EMPRESA
    private Map<String, Object> mapEmpresa(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("emp_id", empresa.getEmp_id());
        empresaMap.put("emp_razon", empresa.getEmp_razon());
        return empresaMap;
    }

    //PERSONA
    private Map<String, Object> mapPersona(Persona persona) {
        if (persona == null) {
            return null;
        }

        Map<String, Object> personaMap = new LinkedHashMap<>();
        personaMap.put("per_id", persona.getPer_id());
        personaMap.put("per_apellido", persona.getPer_apellido());
        return personaMap;
    }

    private Map<String, Object> mapRevisor(Revisor revisor) {
        if (revisor == null) {
            return null;
        }

        Map<String, Object> revisorMap = new LinkedHashMap<>();
        revisorMap.put("rev_id", revisor.getRev_id());
        revisorMap.put("rev_apellido", revisor.getRev_apellido());
        return revisorMap;
    }

    private Map<String, Object> mapRevisorInterno(Revisor revisor) {
        if (revisor == null) {
            return null;
        }

        Map<String, Object> revisorInternoMap = new LinkedHashMap<>();
        revisorInternoMap.put("rev_id", revisor.getRev_id());
        revisorInternoMap.put("rev_apellido", revisor.getRev_apellido());
        return revisorInternoMap;
    }
    private Map<String, Object> mapTipoAdjunto(TipoAdjunto tipoAdjunto) {
        if (tipoAdjunto == null) {
            return null;
        }

        Map<String, Object> tipoAdjuntoMap = new LinkedHashMap<>();
        tipoAdjuntoMap.put("tad_id", tipoAdjunto.getTad_id());
        tipoAdjuntoMap.put("tad_nombre", tipoAdjunto.getTad_nombre());
        tipoAdjuntoMap.put("tad_cod", tipoAdjunto.getTad_cod());
        tipoAdjuntoMap.put("tad_activo", tipoAdjunto.isTad_activo());
        return tipoAdjuntoMap;
    }




    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<MedioDocumento> mediosDocumentos = medioDocumentoService.getAllMedioDocumento();

            if (mediosDocumentos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medios Documento.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(mediosDocumento);
            List<Map<String, Object>> medioDocumentoDTO = new ArrayList<>();
            for (MedioDocumento medioDocumento : mediosDocumentos) {
                Map<String, Object> medioDocuementoMap = mapMedioDocumento(medioDocumento);
                medioDocumentoDTO.add(medioDocuementoMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("medioDocumento", medioDocumentoDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios Documento: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioDocumentoPorId(@PathVariable Integer id) {
        try {
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                return ResponseEntity.ok(medioDocumentoExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Medio Documento. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<MedioDocumento> crearMedioDocumento(@RequestBody Map<String, Object> requestData) {
        try {

            // Variables para relacionar
            Integer mha_id = (Integer) requestData.get("mha_id"); // ID Habilitación MDE
            Integer tad_id = (Integer) requestData.get("tad_id"); // ID Tipo Adjunto
            Integer rev_id = (Integer) requestData.get("rev_id"); // ID Revisor

            // Validaciones
            if (mha_id == null) {
                throw new IllegalArgumentException("El ID de Habilitación de MDE es obligatorio.");
            }
            if (tad_id == null) {
                throw new IllegalArgumentException("El ID del Tipo de Adjunto es obligatorio.");
            }
            if (rev_id == null) {
                throw new IllegalArgumentException("El ID del Revisor es obligatorio.");
            }

            // Buscar Habilitación Empresas por su ID
            MedioHabilitacion medioHabilitacion = medioHabilitacionService.buscarmedioHabilitacionPorId(mha_id);
            if (medioHabilitacion == null) {
                throw new IllegalArgumentException("No se encontró una MDE con el ID: " + mha_id);
            }

            // Buscar Tipo Adjunto por su ID
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(tad_id);
            if (tipoAdjunto == null) {
                throw new IllegalArgumentException("No se encontró un Tipo de Adjunto con el ID: " + tad_id);
            }


            // Extraer valores adicionales
            Integer mdo_adjunto_orden = (Integer) requestData.get("mdo_adjunto_orden");
            String mdo_adjunto_fecha_str = (String) requestData.get("mdo_adjunto_fecha");

            if (mdo_adjunto_orden == null) {
                throw new IllegalArgumentException("El orden del adjunto es obligatorio.");
            }
            if (mdo_adjunto_fecha_str == null) {
                throw new IllegalArgumentException("La fecha del adjunto es obligatoria.");
            }

            // Convertir la fecha de String a Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date mdo_adjunto_fecha;
            try {
                mdo_adjunto_fecha = formatter.parse(mdo_adjunto_fecha_str);
            } catch (ParseException e) {
                throw new IllegalArgumentException("El formato de la fecha del adjunto no es válido: " + mdo_adjunto_fecha_str);
            }

            // Filtrar revisores por rev_aprob_emp
            Revisor revisor = revisorService.buscarRevisorPorId(rev_id);
            if (revisor == null || !revisor.isRev_aprob_mde() && !revisor.isRev_renov_mde()) {
                throw new IllegalArgumentException("No se encontró un Revisor aprobado con el ID: " + rev_id);
            }


            // Crear la entidad de HabilitacionDocumento y establecer las relaciones
            MedioDocumento medioDocumento = new MedioDocumento();
            medioDocumento.setMedioHabilitacion(medioHabilitacion);
            medioDocumento.setTipoAdjunto(tipoAdjunto);
            medioDocumento.setMdo_adjunto_orden(mdo_adjunto_orden);
            medioDocumento.setMdo_adjunto_fecha(mdo_adjunto_fecha);
            medioDocumento.setRevisor(revisor);

            // Llamar al servicio para crear el Habilitación Documento
            MedioDocumento nuevoMedioDocumento = medioDocumentoService.createMedioDocumento(medioDocumento);

            return new RespuestaDTO<>(nuevoMedioDocumento, "MDE creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo MDE: " + e.getMessage());
        } catch (Exception e) {
            // Capturar otras excepciones
            e.printStackTrace();
            return new RespuestaDTO<>(null, "Error al crear un nuevo MDE: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarMedioDocumento(@PathVariable Integer id, @RequestBody MedioDocumento medioDocumento) {
        try {
            // Lógica para modificar el Medio Documento
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioDocumentoExistente.setMedioHabilitacion(medioDocumento.getMedioHabilitacion());
            medioDocumentoExistente.setTipoAdjunto(medioDocumento.getTipoAdjunto());
            medioDocumentoExistente.setMdo_adjunto_orden(medioDocumento.getMdo_adjunto_orden());
            medioDocumentoExistente.setMdo_adjunto_fecha(medioDocumento.getMdo_adjunto_fecha());
            medioDocumentoExistente.setRevisor(medioDocumento.getRevisor());

            // Agrega más propiedades según tu modelo
            medioDocumentoService.updateMedioDocumento(medioDocumentoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Medio Documento. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarMedioDocumento(@PathVariable Integer id) {
        try {
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                medioDocumentoService.deleteMedioDocumentoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Medio Documento eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Medio Documento. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
