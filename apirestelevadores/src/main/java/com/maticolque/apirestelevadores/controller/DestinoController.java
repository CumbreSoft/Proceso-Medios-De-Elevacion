package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.DestinoDTO;
import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
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

            // Obtener la lista de Destinos
            List<Destino> destinos = destinoService.getAllDestino();

            // Verificar la lista de Destinos
            if (destinos.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Destinos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<DestinoDTO> destinosDTO = new ArrayList<>();
            for (Destino destino : destinos) {
                DestinoDTO dto = DestinoDTO.fromEntity(destino);
                destinosDTO.add(dto);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("destinos", destinosDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Destinos: " + e.getMessage())
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
    public ResponseEntity<?> crearDestino(@RequestBody DestinoDTO destinoDTO) {
        try {
            // Validar datos
            if (destinoDTO.getDst_codigo().isEmpty() || destinoDTO.getDst_detalle().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de destino son obligatorios.");
            }

            // Convertir DTO a entidad
            Destino destino = DestinoDTO.toEntity(destinoDTO);

            // Crear Destino
            Destino nuevoDestino = destinoService.createDestino(destino);

            // Convertir entidad a DTO
            DestinoDTO nuevoDestinoDTO = DestinoDTO.fromEntity(nuevoDestino);

            // Mandar respuesta
            RespuestaDTO<DestinoDTO> respuesta = new RespuestaDTO<>(nuevoDestinoDTO, "Destino", "Destino creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Destino: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Destino: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("editar/{id}")
    public ResponseEntity<?> actualizarDestino(@PathVariable Integer id, @RequestBody DestinoDTO destinoDTO) {
        try {
            // Buscar el Destino por ID
            Destino destinoExistente = destinoService.buscarDestinoPorId(id);

            // Verificar si existe el ID del Destino
            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Destino con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Validar datos
            if (destinoDTO.getDst_codigo().isEmpty() || destinoDTO.getDst_detalle().isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("400 BAD REQUEST")
                        .message("Todos los datos del Destino son obligatorios.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
            }

            // Actualizar los campos del Destino
            destinoExistente.setDst_codigo(destinoDTO.getDst_codigo());
            destinoExistente.setDst_detalle(destinoDTO.getDst_detalle());
            destinoExistente.setDst_activo(destinoDTO.isDst_activo());

            // Actualizar Destino
            destinoService.updateDestino(destinoExistente);

            // Mandar respuesta
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al modificar el Destino: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarDestino(@PathVariable Integer id) {
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