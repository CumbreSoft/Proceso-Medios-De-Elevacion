package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaHabilitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/empresaHabilitacion")
public class EmpresaHabilitacionController {

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<EmpresaHabilitacion> empresaHabilitacion = empresaHabilitacionService.getAllEmpresaHabilitacion();

            if (empresaHabilitacion.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Empresas de Habilitacion.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            return ResponseEntity.ok(empresaHabilitacion);

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


    //POST
    @PostMapping
    public RespuestaDTO<EmpresaHabilitacion> crearEmpresaHabilitacion(@RequestBody EmpresaHabilitacion empresaHabilitacion) {
        try {
            // Realizar validación de los datos
            if (empresaHabilitacion.getEha_fecha() == null
            || empresaHabilitacion.getEha_expediente().isEmpty()
                    || empresaHabilitacion.getEha_vto_hab() == null) {

                throw new IllegalArgumentException("Todos los datos de Empresa de Habilitacion son obligatorio.");

            }else if(empresaHabilitacion.getEmpresa().getEmp_id() == 0){

                throw new IllegalArgumentException("La Empresa es obligatoria.");

            }else if(empresaHabilitacion.getRevisor().getRev_id() == 0){

                throw new IllegalArgumentException("El revisor es obligatorio.");
            }

            // Llamar al servicio para crear el destino
            EmpresaHabilitacion nuevoDestino = empresaHabilitacionService.createEmpresaHabilitacion(empresaHabilitacion);
            return new RespuestaDTO<>(nuevoDestino, "Empresa de Habilitacion creado con éxito.");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa de Habilitacion: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear una nueva Empresa de Habilitacion: "  + e.getMessage());
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarEmpresaHabilitacion(@PathVariable Integer id, @RequestBody EmpresaHabilitacion empresaHabilitacion) {
        try {
            // Lógica para modificar el medio de elevación
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
