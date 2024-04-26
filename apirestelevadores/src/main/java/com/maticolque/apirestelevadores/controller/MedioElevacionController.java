package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaService;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import com.maticolque.apirestelevadores.service.TipoMaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
            List<MedioElevacion> mediosElevacion = medioElevacionService.getAllMedioElevacion();

            if (mediosElevacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Medios de Elevación.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> mediosDeElevacionDTO = new ArrayList<>();
            for (MedioElevacion medioElevacion : mediosElevacion) {
                Map<String, Object> mediosDeElevacionMap = new LinkedHashMap<>();

                mediosDeElevacionMap.put("mde_id", medioElevacion.getMde_id());
                mediosDeElevacionMap.put("tiposMaquinas", medioElevacion.getTiposMaquinas());
                mediosDeElevacionMap.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
                mediosDeElevacionMap.put("mde_tipo", medioElevacion.getMde_tipo());
                mediosDeElevacionMap.put("mde_niveles", medioElevacion.getMde_niveles());
                mediosDeElevacionMap.put("mde_planos_aprob", medioElevacion.isMde_planos_aprob());
                mediosDeElevacionMap.put("mde_expte_planos", medioElevacion.getMde_expte_planos());

                // Verificar si la empresa no es nula antes de agregarla al DTO
                if (medioElevacion.getEmpresa() != null) {
                    mediosDeElevacionMap.put("empresa", medioElevacion.getEmpresa());
                } else {
                    mediosDeElevacionMap.put("empresa", null);
                }

                mediosDeElevacionMap.put("mde_activo", medioElevacion.isMde_activo());


                mediosDeElevacionDTO.add(mediosDeElevacionMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("mediosDeElevacion", mediosDeElevacionDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de destinos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }




    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedioElevacionPorId(@PathVariable Integer id) {
        try {
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {

                return ResponseEntity.ok(medioElevacionExistente);
            }

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
    public RespuestaDTO<MedioElevacion> crearMedioElevacion(@RequestBody MedioElevacion medioElevacion) {
        try {
            // Realizar validación de los datos
            if (medioElevacion.getMde_ubicacion().isEmpty() || medioElevacion.getMde_tipo().isEmpty()
                    || medioElevacion.getMde_niveles() == 0) {

                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");
            }
            else if (medioElevacion.getTiposMaquinas().getTma_id() == 0) {
                throw new IllegalArgumentException("La Máquina es obligatoria.");
            }

            // Llamar al servicio para crear el destino
            MedioElevacion nuevoMedioElevacion = medioElevacionService.createMedioElevacion(medioElevacion);

            return new RespuestaDTO<>(nuevoMedioElevacion, "Medio de Elevación creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un Medio de Elevación: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un Medio de Elevación: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarMedioElevacion(@PathVariable Integer id, @RequestBody MedioElevacion medioElevacion) {
        try {
            // Lógica para modificar el Medio de Elevación
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioElevacionExistente.setMde_ubicacion(medioElevacion.getMde_ubicacion());
            medioElevacionExistente.setMde_tipo(medioElevacion.getMde_tipo());
            medioElevacionExistente.setMde_niveles(medioElevacion.getMde_niveles());
            medioElevacionExistente.setMde_activo(medioElevacion.isMde_activo());
            medioElevacionExistente.setTiposMaquinas(medioElevacion.getTiposMaquinas());

            // Agrega más propiedades según tu modelo
            medioElevacionService.updateMedioElevacion(medioElevacionExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Medio de Elevación. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //PUT PARA CAMBIAR LA EMPRESA DE MANTENIMIENTO CON SU ID
    @PutMapping("/editarEmpresaDeMedioDeElevacion/{id}")
    public ResponseEntity<?> actualizarEmpresaDeMedioDeElevacion(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Verificar si el medio de elevación existe
            MedioElevacion mdeExistente = medioElevacionService.buscarMedioElevacionPorId(id);
            if (mdeExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El Medio De Elevación con el ID proporcionado no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener el ID de la nueva empresa (si se proporciona)
            Integer nuevaEmpresaId = (Integer) requestBody.get("emp_id");

            // Obtener el ID de la nueva máquina (si se proporciona)
            Integer nuevoTipoMaquinaId = (Integer) requestBody.get("tma_id");

            // Verificar si se proporcionó un ID de empresa o máquina para actualizar
            if (nuevaEmpresaId == null && nuevoTipoMaquinaId == null) {
                // Si no se proporcionaron campos para actualizar, retornar un mensaje de error
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Debe proporcionar al menos un campo para actualizar (emp_id o tma_id).")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Mensajes de éxito
            StringBuilder mensajeExito = new StringBuilder("Actualización exitosa:");
            if (nuevaEmpresaId != null) {
                Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(nuevaEmpresaId);
                if (nuevaEmpresa == null) {
                    // Si no se encuentra la empresa, agregar un mensaje de error
                    mensajeExito.append(" (Error: No se encontró la empresa con el ID proporcionado).");
                } else {
                    // Actualizar la empresa del medio de elevación
                    mdeExistente.setEmpresa(nuevaEmpresa);
                    mensajeExito.append(" Empresa actualizada correctamente.");
                }
            }
            if (nuevoTipoMaquinaId != null) {
                TipoMaquina nuevoTipoMaquina = tipoMaquinaService.buscartipoMaquinaPorId(nuevoTipoMaquinaId);
                if (nuevoTipoMaquina == null) {
                    // Si no se encuentra la máquina, agregar un mensaje de error
                    mensajeExito.append(" (Error: No se encontró el tipo de máquina con el ID proporcionado).");
                } else {
                    // Actualizar la máquina del medio de elevación
                    mdeExistente.setTiposMaquinas(nuevoTipoMaquina);
                    mensajeExito.append(" Máquina actualizada correctamente.");
                }
            }

            // Actualizar el medio de elevación
            medioElevacionService.updateMedioElevacion(mdeExistente);

            // Construir el mensaje de éxito
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message(mensajeExito.toString())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar la empresa: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }














    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarMedioElevacion(@PathVariable Integer id) {
        try {
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                medioElevacionService.deleteMedioElevacionById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Medio de Elevación eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Medio de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }




    //LISTAR LOS MEDIOS DE ELEVACION CON SU PRIMER INMUEBLE Y EMPRESA
    @GetMapping("/primerInmuebleMedioElevacion")
    public ResponseEntity<?> obtenerPrimerInmuebleMedioElevacion() {
        try {
            // Obtener todos los datos de InmuebleMedioElevacion
            List<InmuebleMedioElevacion> inmuebleMedioElevacionList = medioElevacionService.obtenerTodosLosInmueblesMedioElevacion();

            // Mapear los datos por medio de elevación
            Map<Integer, Map<String, Object>> medioElevacionMap = new HashMap<>();
            for (InmuebleMedioElevacion ime : inmuebleMedioElevacionList) {
                MedioElevacion medioElevacion = ime.getMedioElevacion();
                int mdeId = medioElevacion.getMde_id();
                if (!medioElevacionMap.containsKey(mdeId)) {
                    Map<String, Object> medioElevacionDTO = mapMedioElevacion(medioElevacion);
                    medioElevacionDTO.put("inmueble", mapInmueble(ime.getInmueble()));
                    medioElevacionMap.put(mdeId, medioElevacionDTO);
                }
            }

            // Convertir medioElevacionMap a List<Map<String, Object>>
            List<Map<String, Object>> medioElevacionDTOList = new ArrayList<>(medioElevacionMap.values());

            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioElevacionesInmueble", medioElevacionDTOList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios de Elevación con su primer Inmueble: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    // Método para mapear los datos del medio de elevación a un DTO
    private Map<String, Object> mapMedioElevacion(MedioElevacion medioElevacion) {
        Map<String, Object> medioElevacionDTO = new LinkedHashMap<>();
        medioElevacionDTO.put("mde_id", medioElevacion.getMde_id());
        medioElevacionDTO.put("tiposMaquinas", medioElevacion.getTiposMaquinas());
        medioElevacionDTO.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
        medioElevacionDTO.put("mde_tipo", medioElevacion.getMde_tipo());
        medioElevacionDTO.put("mde_niveles", medioElevacion.getMde_niveles());
        medioElevacionDTO.put("mde_planos_aprob", medioElevacion.isMde_planos_aprob());
        medioElevacionDTO.put("mde_expte_planos", medioElevacion.getMde_expte_planos());
        medioElevacionDTO.put("mde_activo", medioElevacion.isMde_activo());
        medioElevacionDTO.put("empresa", medioElevacion.getEmpresa()); // Incluir la empresa asociada
        return medioElevacionDTO;
    }

    // Método para mapear los datos del inmueble a un DTO
    private Map<String, Object> mapInmueble(Inmueble inmueble) {
        Map<String, Object> inmuebleDTO = new LinkedHashMap<>();
        inmuebleDTO.put("inm_id", inmueble.getInm_id());
        inmuebleDTO.put("inm_padron", inmueble.getInm_padron());
        inmuebleDTO.put("destino", inmueble.getDestino());
        inmuebleDTO.put("inm_direccion", inmueble.getInm_direccion());
        inmuebleDTO.put("distrito", inmueble.getDistrito());
        inmuebleDTO.put("inm_cod_postal", inmueble.getInm_cod_postal());
        inmuebleDTO.put("inm_activo", inmueble.isInm_activo());
        return inmuebleDTO;
    }































    /*LISTAR TODOS LOS MEDIOS DE ELEVACION CON SU LISTA DE [INMUEBLES]
    @GetMapping("/medioDeElevacionConListInmuebles")
    public Map<String, Object> obtenerMediosDeElevacionConInmuebles() {

        Map<String, Object> response = new HashMap<>();

        // Obtener todos los datos de InmuebleMedioDeElevacion
        List<InmuebleMedioElevacion> inmuebleMediosDeElevacion = medioElevacionService.obtenerPrimeroLosDatosDeInmueblePersona();

        // Mapear los datos por Medio De Elevacion
        Map<Integer, Map<String, Object>> mediosDeElevacionMap = new HashMap<>();

        // Agregar inmuebles a mediosDeElevacionMap
        for (InmuebleMedioElevacion inmuebleMedioElevacion : inmuebleMediosDeElevacion) {
            MedioElevacion medioElevacion = inmuebleMedioElevacion.getMedioElevacion();

            int mdeID = medioElevacion.getMde_id();

            if (!mediosDeElevacionMap.containsKey(mdeID)) {
                Map<String, Object> mdeMap = mapMedioElevacion(medioElevacion);
                mdeMap.put("inmuebles", new ArrayList<>());
                mediosDeElevacionMap.put(mdeID, mdeMap);
            }

            Map<String, Object> mdeinmueblesMap = mediosDeElevacionMap.get(mdeID);
            List<Map<String, Object>> inmuebles = (List<Map<String, Object>>) mdeinmueblesMap.get("inmuebles");
            Map<String, Object> inmuebleMap = mapInmueble(inmuebleMedioElevacion.getInmueble());
            inmuebles.add(inmuebleMap);
        }

        // Convertir personasMap a List<Map<String, Object>>
        List<Map<String, Object>> mediosDeElevacionDTO = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : mediosDeElevacionMap.entrySet()) {
            mediosDeElevacionDTO.add(entry.getValue());
        }

        // Agregar la lista completa al objeto de respuesta
        response.put("mediosDeElevacionInmuebles", mediosDeElevacionDTO);

        return response;
    }


    // Método para mapear los datos de la persona a un DTO
    private Map<String, Object> mapMedioElevacion(MedioElevacion medioElevacion) {
        Map<String, Object> medioElevacionDTO = new LinkedHashMap<>();
        medioElevacionDTO.put("mde_id", medioElevacion.getMde_id());
        medioElevacionDTO.put("tiposMaquinas", medioElevacion.getTiposMaquinas());
        medioElevacionDTO.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
        medioElevacionDTO.put("mde_tipo", medioElevacion.getMde_tipo());
        medioElevacionDTO.put("mde_niveles", medioElevacion.getMde_niveles());
        medioElevacionDTO.put("mde_activo", medioElevacion.isMde_activo());
        return medioElevacionDTO;
    }


    // Método para mapear los datos del inmueble a un DTO
    private Map<String, Object> mapInmueble(Inmueble inmueble) {
        Map<String, Object> inmuebleMap = new LinkedHashMap<>();
        inmuebleMap.put("inm_id", inmueble.getInm_id());
        inmuebleMap.put("inm_padron", inmueble.getInm_padron());
        // Agregar otros campos de inmueble si es necesario
        return inmuebleMap;
    }*/

    //Este metodo agregar al Service
    /*public List<InmuebleMedioElevacion> obtenerPrimeroLosDatosDeInmueblePersona() {
        return inmuebleMedioElevacionRepository.findAll();
    }*/


















    //LISTAR TODOS LOS MEDIOS DE ELEVACION CON SU LISTA DE [INMUEBLES] Y SU EMPRESA
    /*@GetMapping("/medioDeElevacionConListInmuebles")
    public Map<String, Object> obtenerMediosDeElevacionConInmuebles() {
        Map<String, Object> response = new HashMap<>();

        List<InmuebleMedioElevacion> inmuebleMediosDeElevacion = medioElevacionService.obtenerPrimeroLosDatosDeInmueblePersona();

        Map<Integer, Map<String, Object>> mediosDeElevacionMap = new HashMap<>();

        for (InmuebleMedioElevacion inmuebleMedioElevacion : inmuebleMediosDeElevacion) {
            MedioElevacion medioElevacion = inmuebleMedioElevacion.getMedioElevacion();
            int mdeID = medioElevacion.getMde_id();

            if (!mediosDeElevacionMap.containsKey(mdeID)) {
                Map<String, Object> mdeMap = mapMedioElevacion(medioElevacion);
                mdeMap.put("inmuebles", new ArrayList<>());
                mediosDeElevacionMap.put(mdeID, mdeMap);
            }

            Map<String, Object> mdeinmueblesMap = mediosDeElevacionMap.get(mdeID);
            List<Map<String, Object>> inmuebles = (List<Map<String, Object>>) mdeinmueblesMap.get("inmuebles");
            Map<String, Object> inmuebleMap = mapInmueble(inmuebleMedioElevacion.getInmueble());
            inmuebles.add(inmuebleMap);

            // Agregar información de la empresa
            Empresa empresa = medioElevacion.getEmpresa();
            Map<String, Object> empresaMap = mapEmpresa(empresa);
            mdeinmueblesMap.put("empresa", empresaMap);
        }

        List<Map<String, Object>> mediosDeElevacionDTO = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : mediosDeElevacionMap.entrySet()) {
            mediosDeElevacionDTO.add(entry.getValue());
        }

        response.put("mediosDeElevacionInmuebles", mediosDeElevacionDTO);

        return response;
    }

    private Map<String, Object> mapEmpresa(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("emp_id", empresa.getEmp_id());
        empresaMap.put("emp_razon", empresa.getEmp_razon());
        // Agregar otros campos de empresa si es necesario
        return empresaMap;
    }



    // Método para mapear los datos de la persona a un DTO
    private Map<String, Object> mapMedioElevacion(MedioElevacion medioElevacion) {
        Map<String, Object> medioElevacionDTO = new LinkedHashMap<>();
        medioElevacionDTO.put("mde_id", medioElevacion.getMde_id());
        medioElevacionDTO.put("tiposMaquinas", medioElevacion.getTiposMaquinas());
        medioElevacionDTO.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
        medioElevacionDTO.put("mde_tipo", medioElevacion.getMde_tipo());
        medioElevacionDTO.put("mde_niveles", medioElevacion.getMde_niveles());
        medioElevacionDTO.put("mde_activo", medioElevacion.isMde_activo());
        return medioElevacionDTO;
    }


    // Método para mapear los datos del inmueble a un DTO
    private Map<String, Object> mapInmueble(Inmueble inmueble) {
        Map<String, Object> inmuebleMap = new LinkedHashMap<>();
        inmuebleMap.put("inm_id", inmueble.getInm_id());
        inmuebleMap.put("inm_padron", inmueble.getInm_padron());
        // Agregar otros campos de inmueble si es necesario
        return inmuebleMap;
    }*/








}