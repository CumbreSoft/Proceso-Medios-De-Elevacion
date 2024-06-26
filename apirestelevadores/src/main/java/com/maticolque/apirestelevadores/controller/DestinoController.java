package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.repository.DestinoRepository;
import com.maticolque.apirestelevadores.service.DestinoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/destino")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private DestinoRepository destinoRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Destino> destinos = destinoService.getAllDestino();

            if (destinos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Destinos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<Map<String, Object>> destinosDTO = new ArrayList<>();
            for (Destino destino : destinos) {
                Map<String, Object> destinoMap = new LinkedHashMap<>();
                destinoMap.put("dst_id", destino.getDst_id());
                destinoMap.put("dst_codigo", destino.getDst_codigo());
                destinoMap.put("dst_detalle", destino.getDst_detalle());
                destinoMap.put("dst_activo", destino.isDst_activo());

                destinosDTO.add(destinoMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("destinos", destinosDTO);

            //return ResponseEntity.ok(destinos);
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
    public ResponseEntity<?> buscarDestinoPorId(@PathVariable Integer id) {
        try {
            Destino destinoExistente = destinoService.buscarDestinoPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(destinoExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el destino. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }

    //POST
    @PostMapping
    public RespuestaDTO<Destino> crearDestino(@RequestBody Destino destino) {
        try {
            // Realizar validación de los datos
            if (destino.getDst_codigo().isEmpty() || destino.getDst_detalle().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de destino son obligatorio.");
            }

            // Llamar al servicio para crear el destino
            Destino nuevoDestino = destinoService.createDestino(destino);
            return new RespuestaDTO<>(nuevoDestino, "Destino creado con éxito.");
        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo destino: " + e.getMessage());
        } catch (Exception e) {
            // Capturar otras excepciones
            e.printStackTrace();
            return new RespuestaDTO<>(null, "Error al crear un nuevo destino: " + e.getMessage());
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarDestino(@PathVariable Integer id, @RequestBody Destino destino) {
        try {
            // Lógica para modificar el destino
            Destino destinoExistente = destinoService.buscarDestinoPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            destinoExistente.setDst_codigo(destino.getDst_codigo());
            destinoExistente.setDst_detalle(destino.getDst_detalle());
            destinoExistente.setDst_activo(destino.isDst_activo());

            destinoService.updateDestino(destinoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Destino."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> elimimarDestino(@PathVariable Integer id) {
        try {
            Destino destinoExistente = destinoService.buscarDestinoPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                destinoService.deleteDestinoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Destino eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el destino. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST DATOS PRECARGADOS
    @PostConstruct
    public void init() {
        // Verificar si ya hay datos cargados en la base de datos
        if (destinoRepository.count() == 0) {

            // Si no hay datos, cargar datos predefinidos
            Destino destino1 = new Destino();
            destino1.setDst_id(1);
            destino1.setDst_codigo("1");
            destino1.setDst_detalle("VIVIENDA");
            destino1.setDst_activo(true);

            Destino destino2 = new Destino();
            destino2.setDst_id(2);
            destino2.setDst_codigo("2");
            destino2.setDst_detalle("COMERCIO");
            destino2.setDst_activo(true);

            Destino destino3 = new Destino();
            destino3.setDst_id(3);
            destino3.setDst_codigo("3");
            destino3.setDst_detalle("DEPOSITO");
            destino3.setDst_activo(true);

            destinoRepository.save(destino1);
            destinoRepository.save(destino2);
            destinoRepository.save(destino3);
        }
    }
}