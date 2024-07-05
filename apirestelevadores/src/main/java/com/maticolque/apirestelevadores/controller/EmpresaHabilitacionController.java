package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /* ******************** METODOS ********************
    * Estos metodos sirven para traer solo los datos que
    * necesito de cada Clase*/

    //EMPRESA
    private Map<String, Object> mapEmpresa(Empresa empresa) {

        if (empresa == null) {
            return null;
        }

        Map<String, Object> empresaDTO = new LinkedHashMap<>();
        empresaDTO.put("emp_id", empresa.getEmp_id());
        empresaDTO.put("emp_razon", empresa.getEmp_razon());
        return empresaDTO;
    }

    //REVISOR
    private Map<String, Object> mapRevisor(Revisor revisor) {
        if (revisor == null) {
            return null;
        }

        Map<String, Object> revisorMap = new LinkedHashMap<>();
        revisorMap.put("rev_id", revisor.getRev_id());
        revisorMap.put("rev_apellido", revisor.getRev_apellido());
        return revisorMap;
    }
    /* ******************** METODOS ******************** */


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {

            // Obtener todas las empresas de habilitación
            List<EmpresaHabilitacion> empresasHabilitacion = empresaHabilitacionService.getAllEmpresaHabilitacion();

            if (empresasHabilitacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresas de Habilitacion.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Definir el formato de fecha deseado
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


            /* Crear lista de mapas para la respuesta
            1)--> Creo una lista llamada empresaHDTO que contendrá mapas (Map) con claves String y valores Object.
            Cada mapa representará una empresa de habilitación con sus atributos correspondientes.
            2)--> Inicio un bucle for-each que iterará sobre cada EmpresaHabilitacion en la lista empresasHabilitacion.
            3)--> Creo un nuevo mapa llamado empresaHMap que almacenará los datos de una empresa de habilitación.*/
            List<Map<String, Object>> empresaHDTO = new ArrayList<>();
            for (EmpresaHabilitacion empresaHabilitacion : empresasHabilitacion) {
                Map<String, Object> empresaHMap = new LinkedHashMap<>();

                // Formatear las fechas antes de agregarlas al mapa
                String ehaFechaFormatted = dateFormat.format(empresaHabilitacion.getEha_fecha());
                String ehaVtoHabFormatted = dateFormat.format(empresaHabilitacion.getEha_vto_hab());

                empresaHMap.put("eha_id", empresaHabilitacion.getEha_id());
                empresaHMap.put("eha_fecha", ehaFechaFormatted);
                empresaHMap.put("empresa",mapEmpresa(empresaHabilitacion.getEmpresa()));
                empresaHMap.put("eha_expediente",empresaHabilitacion.getEha_expediente());
                empresaHMap.put("eha_habilitada",empresaHabilitacion.isEha_habilitada());
                empresaHMap.put("eha_vto_hab", ehaVtoHabFormatted);
                empresaHMap.put("revisor", mapRevisor(empresaHabilitacion.getRevisor()));
                empresaHMap.put("eha_activo",empresaHabilitacion.isEha_activo());

                empresaHDTO.add(empresaHMap);
            }


            //Crear un nuevo mapa llamado response para estructurar la respuesta que se enviará como parte de la respuesta HTTP.
            Map<String, Object> response = new HashMap<>();
            response.put("habilitacionEmpresas", empresaHDTO); //Agregar la lista

            //Crear y devolver una respuesta HTTP 200 OK con el contenido del mapa response.
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Empresas de Habilitacion: " + e.getMessage())
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

    @PostMapping
    public RespuestaDTO<EmpresaHabilitacion> crearEmpresaHabilitacion(@RequestBody EmpresaHabilitacion empresaHabilitacion) {
        try {
            // Realizar validación de los datos
            if (empresaHabilitacion.getEha_fecha() == null
                    || empresaHabilitacion.getEha_expediente().isEmpty()
                    || empresaHabilitacion.getEha_vto_hab() == null) {

                throw new IllegalArgumentException("Todos los datos de Empresa de Habilitacion son obligatorios.");

            } else if (empresaHabilitacion.getEmpresa().getEmp_id() == 0) {

                throw new IllegalArgumentException("La Empresa es obligatoria.");

            } else if (empresaHabilitacion.getRevisor().getRev_id() == 0) {

                throw new IllegalArgumentException("El Revisor es obligatorio.");
            }

            // Verificar si la empresa existe
            Empresa empresa = empresaService.buscarEmpresaPorId(empresaHabilitacion.getEmpresa().getEmp_id());
            if (empresa == null) {
                throw new IllegalArgumentException("La Empresa con ID " + empresaHabilitacion.getEmpresa().getEmp_id() + " no existe.");
            }

            // Verificar si el revisor existe
            Revisor revisor = revisorService.buscarRevisorPorId(empresaHabilitacion.getRevisor().getRev_id());
            if (revisor == null) {
                throw new IllegalArgumentException("El Revisor con ID " + empresaHabilitacion.getRevisor().getRev_id() + " no existe.");
            }

            // Verificar si la empresa tiene al menos una persona asociada
            boolean tienePersonas = empresaPersonaService.verificarRelacionesConPersonas(empresaHabilitacion.getEmpresa().getEmp_id());
            if (!tienePersonas) {
                throw new IllegalArgumentException("La Empresa debe tener al menos una persona asociada antes de ser habilitada.");
            }

            // Llamar al servicio para crear el destino
            EmpresaHabilitacion nuevoDestino = empresaHabilitacionService.createEmpresaHabilitacion(empresaHabilitacion);
            return new RespuestaDTO<>(nuevoDestino, "Empresa de Habilitacion creada con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            throw new IllegalArgumentException("Error al crear una nueva Empresa de Habilitacion: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa de Habilitacion: " + e.getMessage());
        }
    }


    /*PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarEmpresaHabilitacion(@PathVariable Integer id, @RequestBody EmpresaHabilitacion empresaHabilitacion) {
        try {
            // Lógica para modificar la Empresa de Habilitación
            EmpresaHabilitacion empresaHabilitacionExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);

            if (empresaHabilitacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            empresaHabilitacionExistente.setEha_fecha(empresaHabilitacion.getEha_fecha());
            empresaHabilitacionExistente.setEmpresa(empresaHabilitacion.getEmpresa());
            empresaHabilitacionExistente.setEha_expediente(empresaHabilitacion.getEha_expediente());
            empresaHabilitacionExistente.setEha_habilitada(empresaHabilitacion.isEha_habilitada());
            empresaHabilitacionExistente.setEha_vto_hab(empresaHabilitacion.getEha_vto_hab());
            empresaHabilitacionExistente.setRevisor(empresaHabilitacion.getRevisor());

            empresaHabilitacionService.updateEmpresaHabilitacion(empresaHabilitacionExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Empresa de Habilitacion."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }*/

    //PUT EDITAR CON ID
    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarDatosEmpresaHabilitacion(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        try {

            // Buscar el Empresa Habilitacion por ID
            EmpresaHabilitacion eHExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);
            if (eHExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La Empresa Habilitación con el ID proporcionado no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar propiedades básicas si están presentes

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (requestBody.containsKey("eha_fecha")) {
                try {
                    Date ehaFecha = dateFormat.parse((String) requestBody.get("eha_fecha"));
                    eHExistente.setEha_fecha(ehaFecha);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }
            if (requestBody.containsKey("eha_expediente")) {
                eHExistente.setEha_expediente((String) requestBody.get("eha_expediente"));
            }
            if (requestBody.containsKey("eha_habilitada")) {
                eHExistente.setEha_habilitada((Boolean) requestBody.get("eha_habilitada"));
            }
            if (requestBody.containsKey("eha_vto_hab")) {
                try {
                    Date eha_vto_hab = dateFormat.parse((String) requestBody.get("eha_vto_hab"));
                    eHExistente.setEha_vto_hab(eha_vto_hab);
                } catch (ParseException e) {
                    // Manejar el error de formato de fecha
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de fecha incorrecto.");
                }
            }
            if (requestBody.containsKey("eha_activo")) {
                eHExistente.setEha_activo((Boolean) requestBody.get("eha_activo"));
            }

            // Verificar y actualizar Empresa si se proporciona
            /*Integer nuevaEmpresaId = (Integer) requestBody.get("emp_id");
            if (nuevaEmpresaId != null) {
                Empresa nuevaEmpresa = empresaService.buscarEmpresaPorId(nuevaEmpresaId);
                if (nuevaEmpresa == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La empresa con el ID proporcionado no existe.");
                }
                eHExistente.setEmpresa(nuevaEmpresa);
            }*/

            // Verificar y actualizar Revisor si se proporciona
            Integer nuevoRevisorId = (Integer) requestBody.get("rev_id");
            if (nuevoRevisorId != null) {
                Revisor nuevoRevisor = revisorService.buscarRevisorPorId(nuevoRevisorId);
                if (nuevoRevisor == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El Revisor con el ID proporcionado no existe.");
                }
                eHExistente.setRevisor(nuevoRevisor);
            }

            // Guardar los cambios
            empresaHabilitacionService.updateEmpresaHabilitacion(eHExistente);

            return ResponseEntity.status(HttpStatus.OK).body("La Empresa Habilitación se actualizó correctamente.");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar la Empresa de Habilitacion."+ e.getMessage())
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
