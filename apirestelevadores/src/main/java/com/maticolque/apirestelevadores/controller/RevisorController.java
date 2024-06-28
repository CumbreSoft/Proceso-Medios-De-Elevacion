package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.service.RevisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/revisor")
public class RevisorController {

    @Autowired
    private RevisorService revisorService;


    //GET
    /*@GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Revisor> revisores = revisorService.getAllRevisor();

            if (revisores.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Revisores.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(revisor);

            List<Map<String, Object>> revisoresDTO = new ArrayList<>();
            for (Revisor revisor : revisores) {
                Map<String, Object> revisorMap = new LinkedHashMap<>();

                revisorMap.put("rev_id", revisor.getRev_id());
                revisorMap.put("rev_apellido", revisor.getRev_apellido());
                revisorMap.put("rev_nombre", revisor.getRev_nombre());
                revisorMap.put("rev_cuit", revisor.getRev_cuit());
                revisorMap.put("rev_tipodoc", revisor.getRev_tipodoc());
                revisorMap.put("rev_numdoc", revisor.getRev_numdoc());
                revisorMap.put("rev_correo", revisor.getRev_correo());
                revisorMap.put("rev_telefono", revisor.getRev_telefono());
                revisorMap.put("rev_usuario_sayges", revisor.getRev_usuario_sayges());
                revisorMap.put("rev_aprob_mde", revisor.isRev_aprob_mde());
                revisorMap.put("rev_renov_mde", revisor.isRev_renov_mde());
                revisorMap.put("rev_aprob_emp", revisor.isRev_aprob_emp());
                revisorMap.put("rev_activo", revisor.isRev_activo());

                revisoresDTO.add(revisorMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("revisores", revisoresDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Revisores: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }*/


    // GET: Listar revisores basado en un parámetro
    @GetMapping("parametro/{param}")
    public ResponseEntity<?> listarRevisoresPorParametro(@PathVariable int param) {

        // Validar que el parámetro sea 0, 1 o 2
        if (param < 0 || param > 2) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("400 BAD REQUEST")
                    .message("El parámetro debe ser 0, 1 o 2.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        try {
            List<Revisor> revisores = revisorService.getRevisoresByParameter(param);

            if (revisores.isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontraron Revisores según el criterio especificado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> revisoresDTO = new ArrayList<>();
            for (Revisor revisor : revisores) {
                Map<String, Object> revisorMap = new LinkedHashMap<>();

                revisorMap.put("rev_id", revisor.getRev_id());
                revisorMap.put("rev_apellido", revisor.getRev_apellido());
                revisorMap.put("rev_nombre", revisor.getRev_nombre());
                revisorMap.put("rev_cuit", revisor.getRev_cuit());
                revisorMap.put("rev_tipodoc", revisor.getRev_tipodoc());
                revisorMap.put("rev_numdoc", revisor.getRev_numdoc());
                revisorMap.put("rev_correo", revisor.getRev_correo());
                revisorMap.put("rev_telefono", revisor.getRev_telefono());
                revisorMap.put("rev_usuario_sayges", revisor.getRev_usuario_sayges());
                revisorMap.put("rev_aprob_mde", revisor.isRev_aprob_mde());
                revisorMap.put("rev_renov_mde", revisor.isRev_renov_mde());
                revisorMap.put("rev_aprob_emp", revisor.isRev_aprob_emp());
                revisorMap.put("rev_activo", revisor.isRev_activo());

                revisoresDTO.add(revisorMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("revisores", revisoresDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Revisores: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    /*@GetMapping("/{id}")
    public ResponseEntity<?> buscarRevisorPorId(@PathVariable Integer id) {
        try {
            Revisor revisorExistente = revisorService.buscarRevisorPorId(id);

            if (revisorExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                return ResponseEntity.ok(revisorExistente);
            }

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar al Revisor. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }*/


    //POST
    @PostMapping
    public RespuestaDTO<Revisor> crearRevisoor(@RequestBody Revisor revisor){
        try {
            // Realizar validación de los datos
            if (revisor.getRev_nombre().isEmpty() || revisor.getRev_numdoc().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos del Revisor son obligatorios.");
            }

            // Validar que al menos una de las variables sea true
            if (!revisor.isRev_aprob_mde() && !revisor.isRev_renov_mde() && !revisor.isRev_aprob_emp()) {
                throw new IllegalArgumentException("Al menos una de las variables rev_aprob_mde, rev_renov_mde, o rev_aprob_emp debe estar en true.");
            }

            // Llamar al servicio para crear el Revisor
            Revisor nuevoRevisor = revisorService.createRevisor(revisor);
            return new RespuestaDTO<>(nuevoRevisor, "Revisor creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Revisor: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Revisor: " + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarRevisor(@PathVariable Integer id, @RequestBody Revisor revisor) {
        try {
            // Lógica para modificar al Revisor
            Revisor revisorExistente = revisorService.buscarRevisorPorId(id);

            if (revisorExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }


            //Modificar valores
            revisorExistente.setRev_apellido(revisor.getRev_apellido());
            revisorExistente.setRev_nombre(revisor.getRev_nombre());
            revisorExistente.setRev_cuit(revisor.getRev_cuit());
            revisorExistente.setRev_tipodoc(revisor.getRev_tipodoc());
            revisorExistente.setRev_numdoc(revisor.getRev_numdoc());
            revisorExistente.setRev_correo(revisor.getRev_correo());
            revisorExistente.setRev_telefono(revisor.getRev_telefono());
            revisorExistente.setRev_usuario_sayges(revisor.getRev_usuario_sayges());
            revisorExistente.setRev_aprob_mde(revisor.isRev_aprob_mde());
            revisorExistente.setRev_renov_mde(revisor.isRev_renov_mde());
            revisorExistente.setRev_aprob_emp(revisor.isRev_aprob_emp());
            revisorExistente.setRev_activo(revisor.isRev_activo());

            revisorService.updateRevisor(revisorExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el destino. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarRevisor(@PathVariable Integer id){

        try {

            String resultado = revisorService.eliminarRevisorSiNoTieneRelaciones(id);

            if (resultado.equals("Revisor eliminado correctamente.")) {
                return ResponseEntity.ok().body(ErrorDTO.builder().code("200 OK").message(resultado).build());
            } else if (resultado.equals("El ID proporcionado del Revisor no existe.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder().code("404 NOT FOUND").message(resultado).build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().code("400 BAD REQUEST").message(resultado).build());
            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Revisor. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }


    }
}
