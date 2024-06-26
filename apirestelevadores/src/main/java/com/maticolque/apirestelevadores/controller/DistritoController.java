package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.model.TipoMaquina;
import com.maticolque.apirestelevadores.repository.DistritoRepository;
import com.maticolque.apirestelevadores.service.DistritoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/distrito")
public class DistritoController {
    @Autowired
    private DistritoService distritoService;

    @Autowired
    private DistritoRepository distritoRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<Distrito> distrito = distritoService.getAllDistrito();

            if (distrito.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Distritos.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //return ResponseEntity.ok(distrito);

            List<Map<String, Object>> distritoDTO = new ArrayList<>();
            for (Distrito distrito1 : distrito) {
                Map<String, Object> distritoMap = new LinkedHashMap<>();

                distritoMap.put("dis_id", distrito1.getDis_id());
                distritoMap.put("dis_codigo", distrito1.getDis_codigo());
                distritoMap.put("dis_nombre", distrito1.getDis_nombre());
                distritoMap.put("dis_activo", distrito1.isDis_activo());

                distritoDTO.add(distritoMap);

            }

            Map<String, Object> response = new HashMap<>();
            response.put("distritos", distritoDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Distritos: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDistritoPorId(@PathVariable Integer id) {
        try {
            Distrito distritoExistente = distritoService.buscarDistritoPorId(id);

            if (distritoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {
                return ResponseEntity.ok(distritoExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Distrito. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST
    @PostMapping
    public RespuestaDTO<Distrito> crearDistrito(@RequestBody Distrito distrito) {
        try {
            // Realizar validación de los datos
            if (distrito.getDis_codigo() == null || distrito.getDis_nombre().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de Distrito son obligatorios.");
            }

            // Llamar al servicio para crear el Distrito
            Distrito nuevoDestino = distritoService.createDistrito(distrito);
            return new RespuestaDTO<>(nuevoDestino, "Distrito creado con éxito");

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            return new RespuestaDTO<>(null, "Error al crear un nuevo Distrito: " + e.getMessage());

        } catch (Exception e) {
            return new RespuestaDTO<>(null, "Error al crear un nuevo Distrito: "  + e.getMessage());
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarDistrito(@PathVariable Integer id, @RequestBody Distrito distrito) {
        try {
            // Lógica para modificar el Distrito
            Distrito destinoExistente = distritoService.buscarDistritoPorId(id);

            if (destinoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta modificar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            destinoExistente.setDis_nombre(distrito.getDis_nombre());
            destinoExistente.setDis_activo(distrito.isDis_activo());
            destinoExistente.setDis_codigo(distrito.getDis_codigo());

            distritoService.updateDistrito(destinoExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);


        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Distrito."+ e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> elimimarDistrito(@PathVariable Integer id) {
        try {
            Distrito distritoExistente = distritoService.buscarDistritoPorId(id);

            if (distritoExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                distritoService.deleteDistritoById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Distrito eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);
            }
        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Distrito. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }



    //POST DATOS PRECARGADOS
    /*@PostConstruct
    public void init() {
        // Verificar si ya hay datos cargados en la base de datos
        if (distritoRepository.count() == 0) {

            // Si no hay datos, cargar datos predefinidos
            Distrito distrito1 = new Distrito();
            distrito1.setDis_id(1);
            distrito1.setDis_codigo("1");
            distrito1.setDis_nombre("BELG");
            distrito1.setDis_activo(true);

            Distrito distrito2 = new Distrito();
            distrito2.setDis_id(2);
            distrito2.setDis_codigo("2");
            distrito2.setDis_nombre("BUENA");
            distrito2.setDis_activo(true);

            Distrito distrito3 = new Distrito();
            distrito3.setDis_id(3);
            distrito3.setDis_codigo("3");
            distrito3.setDis_nombre("ROSARIO");
            distrito3.setDis_activo(true);

            Distrito distrito4 = new Distrito();
            distrito4.setDis_id(4);
            distrito4.setDis_codigo("4");
            distrito4.setDis_nombre("MOLINA");
            distrito4.setDis_activo(true);

            Distrito distrito5 = new Distrito();
            distrito5.setDis_id(5);
            distrito5.setDis_codigo("5");
            distrito5.setDis_nombre("SEGO");
            distrito5.setDis_activo(true);


            distritoRepository.save(distrito1);
            distritoRepository.save(distrito2);
            distritoRepository.save(distrito3);
            distritoRepository.save(distrito4);
            distritoRepository.save(distrito5);

        }
    }*/
}
