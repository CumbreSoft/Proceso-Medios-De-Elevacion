package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaHabilitacionService;
import com.maticolque.apirestelevadores.service.HabilitacionDocumentoService;
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
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/habilitacionDocumento")
public class HabilitacionDocumentoController {

    @Autowired
    private HabilitacionDocumentoService habilitacionDocumentoService;

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;

    @Autowired
    private RevisorService revisorService;



    //METODOS
    private Map<String, Object> mapHabilitacionDocumento(HabilitacionDocumento habilitacionDocumento) {
        if (habilitacionDocumento == null) {
            return null;
        }

        Map<String, Object> habilitacionDocumentoMap = new LinkedHashMap<>();
        habilitacionDocumentoMap.put("hdo_id", habilitacionDocumento.getHdo_id());
        habilitacionDocumentoMap.put("empresaHabilitacion", mapEmpresaHabilitacion(habilitacionDocumento.getEmpresaHabilitacion()));
        habilitacionDocumentoMap.put("tipoAdjunto", mapTipoAdjunto(habilitacionDocumento.getTipoAdjunto()));
        habilitacionDocumentoMap.put("hdo_adjunto_orden", habilitacionDocumento.getHdo_adjunto_orden());
        habilitacionDocumentoMap.put("hdo_adjunto_fecha", habilitacionDocumento.getHdo_adjunto_fecha());
        habilitacionDocumentoMap.put("revisorInterno", mapRevisorInterno(habilitacionDocumento.getRevisor()));
        return habilitacionDocumentoMap;
    }

    private Map<String, Object> mapEmpresaHabilitacion(EmpresaHabilitacion empresaHabilitacion) {
        if (empresaHabilitacion == null) {
            return null;
        }

        Map<String, Object> empresaHabilitacionMap = new LinkedHashMap<>();
        empresaHabilitacionMap.put("eha_id", empresaHabilitacion.getEha_id());
        empresaHabilitacionMap.put("eha_fecha", empresaHabilitacion.getEha_fecha());
        empresaHabilitacionMap.put("empresa", mapEmpresa(empresaHabilitacion.getEmpresa()));
        empresaHabilitacionMap.put("eha_expediente", empresaHabilitacion.getEha_expediente());
        empresaHabilitacionMap.put("eha_habilitada", empresaHabilitacion.isEha_habilitada());
        empresaHabilitacionMap.put("eha_vto_hab", empresaHabilitacion.getEha_vto_hab());
        empresaHabilitacionMap.put("revisor", mapRevisor(empresaHabilitacion.getRevisor()));
        empresaHabilitacionMap.put("eha_activo", empresaHabilitacion.isEha_activo());
        return empresaHabilitacionMap;
    }

    private Map<String, Object> mapEmpresa(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("emp_id", empresa.getEmp_id());
        empresaMap.put("emp_razon", empresa.getEmp_razon());
        return empresaMap;
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

    //METODOS


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<HabilitacionDocumento> habilitacionDocumentos = habilitacionDocumentoService.getAllHabilitacionDocumento();

            if (habilitacionDocumentos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Habilitacion de Documentos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(habilitacionDocumentos);
            List<Map<String, Object>> habilitacionDocumentoDTO = new ArrayList<>();
            for (HabilitacionDocumento habilitacionDocumento : habilitacionDocumentos) {
                Map<String, Object> habilitacionDocumentoMap = mapHabilitacionDocumento(habilitacionDocumento);
                habilitacionDocumentoDTO.add(habilitacionDocumentoMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionDocumento", habilitacionDocumentoDTO);

            return ResponseEntity.ok(response);



        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Habilitacion de Documentos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarHabilitacionDocumentoPorId(@PathVariable Integer id) {
        try {
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(habilitacionDocumentoExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar la Habilitacion de Documentos. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<HabilitacionDocumento> crearHabilitacionDocumento(@RequestBody Map<String, Object> requestData) {
        try {
            // Variables para relacionar
            Integer eha_id = (Integer) requestData.get("eha_id"); // ID Habilitación Empresas
            Integer tad_id = (Integer) requestData.get("tad_id"); // ID Tipo Adjunto
            Integer rev_id = (Integer) requestData.get("rev_id"); // ID Revisor

            // Validaciones
            if (eha_id == null) {
                throw new IllegalArgumentException("El ID de Habilitación de Empresa es obligatorio.");
            }
            if (tad_id == null) {
                throw new IllegalArgumentException("El ID del Tipo de Adjunto es obligatorio.");
            }
            if (rev_id == null) {
                throw new IllegalArgumentException("El ID del Revisor es obligatorio.");
            }

            // Buscar Habilitación Empresas por su ID
            EmpresaHabilitacion empresaHabilitacion = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(eha_id);
            if (empresaHabilitacion == null) {
                throw new IllegalArgumentException("No se encontró una Habilitación Empresa con el ID: " + eha_id);
            }

            // Buscar Tipo Adjunto por su ID
            TipoAdjunto tipoAdjunto = tipoAdjuntoService.buscarTipoAdjuntoPorId(tad_id);
            if (tipoAdjunto == null) {
                throw new IllegalArgumentException("No se encontró un Tipo de Adjunto con el ID: " + tad_id);
            }

            // Extraer valores adicionales
            Integer hdo_adjunto_orden = (Integer) requestData.get("hdo_adjunto_orden");
            String hdo_adjunto_fecha_str = (String) requestData.get("hdo_adjunto_fecha");

            if (hdo_adjunto_orden == null) {
                throw new IllegalArgumentException("El orden del adjunto es obligatorio.");
            }
            if (hdo_adjunto_fecha_str == null) {
                throw new IllegalArgumentException("La fecha del adjunto es obligatoria.");
            }

            // Convertir la fecha de String a Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date hdo_adjunto_fecha;
            try {
                hdo_adjunto_fecha = formatter.parse(hdo_adjunto_fecha_str);
            } catch (ParseException e) {
                throw new IllegalArgumentException("El formato de la fecha del adjunto no es válido: " + hdo_adjunto_fecha_str);
            }

            // Filtrar revisores por rev_aprob_emp
            Revisor revisor = revisorService.buscarRevisorPorId(rev_id);
            if (revisor == null || !revisor.isRev_aprob_emp()) {
                throw new IllegalArgumentException("No se encontró un Revisor aprobado con el ID: " + rev_id);
            }

            // Crear la entidad de HabilitacionDocumento y establecer las relaciones
            HabilitacionDocumento habilitacionDocumento = new HabilitacionDocumento();
            habilitacionDocumento.setEmpresaHabilitacion(empresaHabilitacion);
            habilitacionDocumento.setTipoAdjunto(tipoAdjunto);
            habilitacionDocumento.setHdo_adjunto_orden(hdo_adjunto_orden);
            habilitacionDocumento.setHdo_adjunto_fecha(hdo_adjunto_fecha);
            habilitacionDocumento.setRevisor(revisor);

            // Llamar al servicio para crear el Habilitación Documento
            HabilitacionDocumento nuevoHabilitacionDocumento = habilitacionDocumentoService.createHabilitacionDocumento(habilitacionDocumento);

            return new RespuestaDTO<>(nuevoHabilitacionDocumento, "Habilitación Documento creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Habilitación de Documentos: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Habilitación de Documentos: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarHabilitacionDocumento(@PathVariable Integer id, @RequestBody HabilitacionDocumento habilitacionDocumento) {
        try {
            // Lógica para modificar Habilitación Documentos
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            habilitacionDocumentoExistente.setEmpresaHabilitacion(habilitacionDocumento.getEmpresaHabilitacion());
            habilitacionDocumentoExistente.setTipoAdjunto(habilitacionDocumento.getTipoAdjunto());
            habilitacionDocumentoExistente.setHdo_adjunto_orden(habilitacionDocumento.getHdo_adjunto_orden());
            habilitacionDocumentoExistente.setHdo_adjunto_fecha(habilitacionDocumento.getHdo_adjunto_fecha());
            habilitacionDocumentoExistente.setRevisor(habilitacionDocumento.getRevisor());

            habilitacionDocumentoService.updateHabilitacionDocumento(habilitacionDocumentoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Habilitacion de Documentos."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarHabilitacionDocumento(@PathVariable Integer id) {
        try {
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                habilitacionDocumentoService.deleteHabilitacionDocumentoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Habilitacion de Documentos eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar la  Habilitacion de Documentos. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
