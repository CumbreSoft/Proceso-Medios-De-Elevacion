package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.InmuebleMedioElevacionService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/inmueblesMDE")
public class InmuebleMedioElevacionController {

    @Autowired
    private InmuebleMedioElevacionService inmuebleMedioElevacionService;

    @Autowired
    private InmuebleService inmuebleService;

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private MedioElevacionService medioElevacionService;

    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            List<InmuebleMedioElevacion> inmuebleMDE = inmuebleMedioElevacionService.getAllInmueblesMDE();
            List<Map<String, Object>> inmuebleMDEDTO = new ArrayList<>();


            for (InmuebleMedioElevacion inmuebleMedioElevacion : inmuebleMDE) {
                Map<String, Object> inmuebleMDEMap = new LinkedHashMap<>();

                inmuebleMDEMap.put("ime_id", inmuebleMedioElevacion.getIme_id());

                // Extraer los datos del Inmueble asociado
                Inmueble inmueble = inmuebleMedioElevacion.getInmueble();
                Map<String, Object> inmuebleMap = new LinkedHashMap<>();
                inmuebleMap.put("inm_id", inmueble.getInm_id());
                inmuebleMap.put("inm_padron", inmueble.getInm_padron());
                inmuebleMap.put("inm_direccion", inmueble.getInm_direccion());
                inmuebleMap.put("inm_cod_postal", inmueble.getInm_cod_postal());
                inmuebleMap.put("distrito", inmueble.getDistrito());
                inmuebleMap.put("destino", inmueble.getDestino());
                inmuebleMap.put("inm_activo", inmueble.isInm_activo());
                inmuebleMDEMap.put("inmueble", inmuebleMap);

                // Extraer los datos del Medio De Elevacion
                MedioElevacion medioElevacion = inmuebleMedioElevacion.getMedioElevacion();
                Map<String, Object> mdeMap = new LinkedHashMap<>();
                mdeMap.put("mde_id", medioElevacion.getMde_id());
                mdeMap.put("tiposMaquinas", medioElevacion.getTiposMaquinas());
                mdeMap.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
                mdeMap.put("mde_tipo", medioElevacion.getMde_tipo());
                mdeMap.put("mde_niveles", medioElevacion.getMde_niveles());
                mdeMap.put("mde_planos_aprob", medioElevacion.isMde_planos_aprob());
                mdeMap.put("mde_expte_planos", medioElevacion.getMde_expte_planos());
                //mdeMap.put("empresa", medioElevacion.getEmpresa());
                // Mapear los datos de la empresa utilizando mapEmpresa
                Map<String, Object> empresaMap = mapEmpresa(medioElevacion.getEmpresa());
                mdeMap.put("empresa", empresaMap);
                mdeMap.put("mde_activo", medioElevacion.isMde_activo());
                inmuebleMDEMap.put("medio de Elevacion", mdeMap);

                inmuebleMDEDTO.add(inmuebleMDEMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("inmueblesMDE", inmuebleMDEDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmueble Medios de Elevación: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarInmuebleMDEPorId(@PathVariable Integer id) {
        try {
            InmuebleMedioElevacion medioElevacion = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (medioElevacion == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(medioElevacion);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Inmueble Medios de Elevación. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    @PostMapping
    public RespuestaDTO<InmuebleMedioElevacion> crearInmuebleMDE(@RequestBody Map<String, Integer> requestData) {
        try {
            Integer inmuebleId = requestData.get("inmuebleId");
            Integer medioElevacionId = requestData.get("medioElevacionId");

            // Validaciones
            if (inmuebleId == null) {
                throw new IllegalArgumentException("El ID del inmueble es obligatorio.");
            }
            if (medioElevacionId == null) {
                throw new IllegalArgumentException("El ID del medio de elevación es obligatorio.");
            }

            // Buscar el inmueble por su ID
            Inmueble inmueble = inmuebleService.buscarInmueblePorId(inmuebleId);
            if (inmueble == null) {
                throw new IllegalArgumentException("No se encontró un inmueble con el ID: " + inmuebleId);
            }

            // Buscar el medio de elevación por su ID
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(medioElevacionId);
            if (medioElevacion == null) {
                throw new IllegalArgumentException("No se encontró un medio de elevación con el ID: " + medioElevacionId);
            }

            // Crear la entidad de InmuebleMedioElevacion y establecer las relaciones
            InmuebleMedioElevacion inmuebleMedioElevacion = new InmuebleMedioElevacion();
            inmuebleMedioElevacion.setInmueble(inmueble);
            inmuebleMedioElevacion.setMedioElevacion(medioElevacion);

            // Llamar al servicio para crear el Inmueble Medios de Elevación
            InmuebleMedioElevacion nuevoInmuebleMedioElevacion = inmuebleMedioElevacionService.createInmuebleMDE(inmuebleMedioElevacion);

            return new RespuestaDTO<>(nuevoInmuebleMedioElevacion, "Inmueble Medio de Elevación creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Elevación: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Medio de Elevación: " + e.getMessage());
        }
    }





    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> modificarInmuebleMDE(@PathVariable Integer id, @RequestBody InmuebleMedioElevacion inmuebleMedioElevacion) {
        try {
            // Lógica para modificar el Inmueble Medios de Elevación
            InmuebleMedioElevacion medioElevacionExistente = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (medioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            medioElevacionExistente.setInmueble(inmuebleMedioElevacion.getInmueble());
            medioElevacionExistente.setMedioElevacion(inmuebleMedioElevacion.getMedioElevacion()  );

            inmuebleMedioElevacionService.updateInmuebleMDE(medioElevacionExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Inmueble Medio de Elevacion."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

        }
    }


    @PutMapping("/editarInmuebleDeMedioElevacion/{id}")
    public ResponseEntity<?> actualizarInmuebleDeMedioElevacion(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {
            // Obtener el nuevo ID del inmueble (inm_id) desde el cuerpo de la solicitud
            Integer nuevoInmuebleId = (Integer) requestBody.get("inm_id");

            // Verificar que el ID del inmueble no sea nulo
            if (nuevoInmuebleId == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("El campo 'inm_id' es obligatorio.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Buscar la relación de InmuebleMedioElevación por ID
            InmuebleMedioElevacion relacionExistente = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            if (relacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró la relación InmuebleMedioElevación con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Buscar el nuevo inmueble por su ID
            Inmueble nuevoInmueble = inmuebleService.buscarInmueblePorIds(nuevoInmuebleId);

            if (nuevoInmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el inmueble con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar la relación con el nuevo inmueble
            relacionExistente.setInmueble(nuevoInmueble);

            // Guardar la relación actualizada
            inmuebleMedioElevacionService.updateInmuebleMDE(relacionExistente);

            // Retornar respuesta de éxito
            ErrorDTO successDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El inmueble en la relación InmuebleMedioElevación se ha actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(successDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar el inmueble en la relación InmuebleMedioElevación: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }





    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarInmuebleMDE(@PathVariable Integer id) {
        try {

            String resultado = inmuebleService.eliminarInmuebleSiNoTieneRelaciones(id);

            if (resultado.equals("Inmueble eliminado correctamente.")) {
                return ResponseEntity.ok().body(ErrorDTO.builder().code("200 OK").message(resultado).build());
            } else if (resultado.equals("El ID proporcionado del Inmueble no existe.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder().code("404 NOT FOUND").message(resultado).build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().code("400 BAD REQUEST").message(resultado).build());
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Inmueble Medio de Elevacion. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    // Método para obtener todos los medios de elevación asociados a un inmueble específico
    @GetMapping("/padron/{inmPadron}/mediosElevacion")
    public ResponseEntity<?> obtenerMediosElevacionPorPadron(@PathVariable("inmPadron") Integer inmPadron) {
        try {
            // Obtener el inmueble por su padron
            Inmueble inmueble = inmuebleService.buscarInmueblePorPadron(inmPadron);
            if (inmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró un inmueble con el padron: " + inmPadron)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener todos los datos de InmuebleMedioElevacion asociados al inmueble
            List<InmuebleMedioElevacion> inmuebleMedioElevacionList = inmuebleMedioElevacionService.obtenerMediosDeElevacionPorInmuebleId(inmueble.getInm_id());

            // Lista para almacenar los medios de elevación asociados al inmueble
            List<Map<String, Object>> mediosElevacionList = new ArrayList<>();
            for (InmuebleMedioElevacion ime : inmuebleMedioElevacionList) {
                Map<String, Object> medioElevacionDTO = mapMedioElevacion(ime.getMedioElevacion());

                // Obtener la empresa asociada al medio de elevación
                Empresa empresa = ime.getMedioElevacion().getEmpresa();
                if (empresa != null) {
                    // Obtener la lista de personas asociadas a la empresa
                    List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresa.getEmp_id());

                    // Mapear los datos de la empresa y las personas a un DTO
                    Map<String, Object> empresaDTO = mapEmpresa(empresa);
                    medioElevacionDTO.put("empresa", empresaDTO);
                }

                mediosElevacionList.add(medioElevacionDTO);
            }

            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("mediosElevacion", mediosElevacionList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios de Elevación para el Inmueble con padron: " + inmPadron + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }



    // Método para obtener el medio de elevación por su ID
    @GetMapping("/medioElevacion/{medioElevacionId}")
    public ResponseEntity<?> obtenerMedioElevacionPorId(@PathVariable("medioElevacionId") Integer medioElevacionId) {
        try {
            // Obtener el InmuebleMedioElevacion asociado al medio de elevación
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(medioElevacionId);

            if (medioElevacion == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El Medio de Elevación con ID: " + medioElevacionId + " no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Mapear los datos del medio de elevación a un DTO
            Map<String, Object> medioElevacionDTO = mapMedioElevacion(medioElevacion);

            // Obtener la empresa asociada al medio de elevación
            Empresa empresa = medioElevacion.getEmpresa();
            if (empresa != null) {
                // Obtener la lista de personas asociadas a la empresa
                List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresa.getEmp_id());

                // Mapear los datos de la empresa y las personas a un DTO
                Map<String, Object> empresaDTO = mapEmpresa(empresa);
                medioElevacionDTO.put("empresa", empresaDTO);
            } else {
                medioElevacionDTO.put("empresa", null);
            }

            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioElevacion", medioElevacionDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener el Medio de Elevación con ID: " + medioElevacionId + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    // Método para mapear los datos del medio de elevación a un DTO
    private Map<String, Object> mapMedioElevacion(MedioElevacion medioElevacion) {
        Map<String, Object> medioElevacionDTO = new LinkedHashMap<>();
        medioElevacionDTO.put("mde_id", medioElevacion.getMde_id());
        medioElevacionDTO.put("tiposMaquinas", mapTiposMaquinas(medioElevacion.getTiposMaquinas()));
        //Descomentar para ver mas datos
        medioElevacionDTO.put("mde_ubicacion", medioElevacion.getMde_ubicacion());
        //medioElevacionDTO.put("mde_tipo", medioElevacion.getMde_tipo());
        //medioElevacionDTO.put("mde_niveles", medioElevacion.getMde_niveles());
        //medioElevacionDTO.put("mde_planos_aprob", medioElevacion.isMde_planos_aprob());
        //medioElevacionDTO.put("mde_expte_planos", medioElevacion.getMde_expte_planos());
        //medioElevacionDTO.put("mde_activo", medioElevacion.isMde_activo());
        medioElevacionDTO.put("empresa", mapEmpresa(medioElevacion.getEmpresa())); // Incluir la empresa asociada
        return medioElevacionDTO;
    }

    //TIPO MAQUINA
    private Map<String, Object> mapTiposMaquinas(TipoMaquina tiposMaquinas) {
        if (tiposMaquinas == null) {
            return null;
        }
        Map<String, Object> tiposMaquinasDTO = new LinkedHashMap<>();
        tiposMaquinasDTO.put("tma_id", tiposMaquinas.getTma_id());
        tiposMaquinasDTO.put("tma_detalle", tiposMaquinas.getTma_detalle());
        return tiposMaquinasDTO;
    }

    //EMPRESA
    private Map<String, Object> mapEmpresa(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        Map<String, Object> empresaDTO = new LinkedHashMap<>();
        empresaDTO.put("emp_id", empresa.getEmp_id());
        empresaDTO.put("emp_razon", empresa.getEmp_razon());

        // Obtener las personas asociadas a la empresa
        List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresa.getEmp_id());

        // Mapear los datos de las personas a un DTO
        List<Map<String, Object>> personasDTOList = new ArrayList<>();
        for (Persona persona : personas) {
            Map<String, Object> personaDTO = mapPersona(persona);
            personasDTOList.add(personaDTO);
        }
        empresaDTO.put("personas", personasDTOList);

        return empresaDTO;
    }

    // Método para mapear los datos de la persona a un DTO
    private Map<String, Object> mapPersona(Persona persona) {
        if (persona == null) {
            return null;
        }

        Map<String, Object> personaDTO = new LinkedHashMap<>();
        personaDTO.put("per_id", persona.getPer_id());
        personaDTO.put("per_apellido", persona.getPer_apellido());
        // Añadir más campos según tus necesidades

        return personaDTO;
    }

}
