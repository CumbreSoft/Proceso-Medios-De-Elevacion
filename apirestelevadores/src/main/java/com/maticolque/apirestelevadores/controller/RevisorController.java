package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.RevisorDTO;
import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.service.RevisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("api/v1/revisor")
public class RevisorController {

    @Autowired
    private RevisorService revisorService;


    // GET: LISTAR REVISORES BASADO EN UN PARAMETRO
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
            // Obtener la lista de Revisores segun el parametro enviado
            List<Revisor> revisores = revisorService.getRevisoresByParameter(param);

            // Verificar la lista de Revisores
            if (revisores.isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontraron Revisores según el criterio especificado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<RevisorDTO> revisoresDTO = new ArrayList<>();
            for (Revisor revisor : revisores) {
                RevisorDTO dto = RevisorDTO.fromEntity(revisor);
                revisoresDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("revisores", revisoresDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Revisores: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearRevisoor(@RequestBody RevisorDTO revisorDTO){
        try {

            // Validar datos
            if (revisorDTO.getRev_apellido().isEmpty() || revisorDTO.getRev_nombre().isEmpty() || revisorDTO.getRev_cuit().isEmpty() ||
                    revisorDTO.getRev_numdoc().isEmpty() || revisorDTO.getRev_correo().isEmpty() || revisorDTO.getRev_telefono().isEmpty() ||
                revisorDTO.getRev_usuario_sayges().isEmpty()) {
                throw new IllegalArgumentException("No se permiten datos vacíos.");
            }

            // Validar que al menos una de las variables sea true
            if (!revisorDTO.isRev_aprob_mde() && !revisorDTO.isRev_renov_mde() && !revisorDTO.isRev_aprob_emp()) {
                throw new IllegalArgumentException("Al menos una de las variables rev_aprob_mde, rev_renov_mde, o rev_aprob_emp debe estar en true.");
            }

            // Convertir DTO a entidad
            Revisor revisor = RevisorDTO.toEntity(revisorDTO);

            // Crear Revisor
            Revisor nuevoRevisor = revisorService.createRevisor(revisor);

            // Convertir entidad a DTO
            RevisorDTO nuevaRevisorDTO = RevisorDTO.fromEntity(nuevoRevisor);

            // Mandar respuesta
            RespuestaDTO<RevisorDTO> respuesta = new RespuestaDTO<>(nuevaRevisorDTO, "Revisor", "Revisor creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Revisor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Revisor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarRevisor(@PathVariable Integer id, @RequestBody RevisorDTO revisorDTO) {
        try {

            // Buscar el Revisor por ID
            Revisor revisorExistente = revisorService.buscarRevisorPorId(id);

            // Verificar si existe el ID del Revisor
            if (revisorExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Revisor con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (revisorDTO.getRev_apellido().isEmpty() || revisorDTO.getRev_nombre().isEmpty() || revisorDTO.getRev_cuit().isEmpty() ||
                    revisorDTO.getRev_numdoc().isEmpty() || revisorDTO.getRev_correo().isEmpty() || revisorDTO.getRev_telefono().isEmpty() ||
                    revisorDTO.getRev_usuario_sayges().isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("No se permiten datos vacíos.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Validar que al menos una de las variables sea true
            if (!revisorDTO.isRev_aprob_mde() && !revisorDTO.isRev_renov_mde() && !revisorDTO.isRev_aprob_emp()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Al menos una de las variables rev_aprob_mde, rev_renov_mde, o rev_aprob_emp debe estar en true.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos del Revisor
            revisorExistente.setRev_apellido(revisorDTO.getRev_apellido());
            revisorExistente.setRev_nombre(revisorDTO.getRev_nombre());
            revisorExistente.setRev_cuit(revisorDTO.getRev_cuit());
            revisorExistente.setRev_tipodoc(revisorDTO.getRev_tipodoc());
            revisorExistente.setRev_numdoc(revisorDTO.getRev_numdoc());
            revisorExistente.setRev_correo(revisorDTO.getRev_correo());
            revisorExistente.setRev_telefono(revisorDTO.getRev_telefono());
            revisorExistente.setRev_usuario_sayges(revisorDTO.getRev_usuario_sayges());
            revisorExistente.setRev_aprob_mde(revisorDTO.isRev_aprob_mde());
            revisorExistente.setRev_renov_mde(revisorDTO.isRev_renov_mde());
            revisorExistente.setRev_aprob_emp(revisorDTO.isRev_aprob_emp());
            revisorExistente.setRev_activo(revisorDTO.isRev_activo());

            // Actualizar Revisor
            revisorService.updateRevisor(revisorExistente);

            // Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar al Revisor. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarRevisor(@PathVariable Integer id) {
        return revisorService.eliminarRevisorSiNoTieneRelaciones(id);
    }
}