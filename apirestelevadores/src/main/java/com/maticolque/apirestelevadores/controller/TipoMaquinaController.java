package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.MedioElevacionReadDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.dto.TipoMaquinaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.TipoMaquinaRepository;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.TipoMaquinaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/tipoMaquina")
public class TipoMaquinaController {

    @Autowired
    private TipoMaquinaService tipoMaquinaService;

    @Autowired
    private TipoMaquinaRepository tipoMaquinaRepository;


    //GET
    @GetMapping
    public ResponseEntity<?> listarTodo() {
        try {
            List<TipoMaquina> tipoMaquinas = tipoMaquinaService.getAllTipoMaquina();

            if (tipoMaquinas.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Tipos de Máquinas.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            List<TipoMaquinaDTO> tipoMaquinasDTO = new ArrayList<>();
            for (TipoMaquina tipoMaquina : tipoMaquinas) {
                TipoMaquinaDTO dto = TipoMaquinaDTO.fromEntity(tipoMaquina);
                tipoMaquinasDTO.add(dto);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("tipoMaquinas", tipoMaquinasDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Tipos de Máquinas: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoMaquinaPorId(@PathVariable Integer id) {
        try {
            TipoMaquina tipoMaquinaExistente = tipoMaquinaService.buscartipoMaquinaPorId(id);

            if (tipoMaquinaExistente == null) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta buscar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            } else {
                return ResponseEntity.ok(tipoMaquinaExistente);
            }
        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al buscar el Tipo de Máquina. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }


    //POST DATOS PRECARGADOS
    @PostConstruct
    public void init() {
        // Verificar si ya hay datos cargados en la base de datos
        if (tipoMaquinaRepository.count() == 0) {
            // Si no hay datos, cargar datos predefinidos
            TipoMaquina tipoMaquina1 = new TipoMaquina();
            tipoMaquina1.setTma_cod(11);
            tipoMaquina1.setTma_detalle("Ascensor");
            tipoMaquina1.setTma_activa(true);

            TipoMaquina tipoMaquina2 = new TipoMaquina();
            tipoMaquina2.setTma_cod(22);
            tipoMaquina2.setTma_detalle("Montacarga");
            tipoMaquina2.setTma_activa(true);

            TipoMaquina tipoMaquina3 = new TipoMaquina();
            tipoMaquina3.setTma_cod(33);
            tipoMaquina3.setTma_detalle("Escalera");
            tipoMaquina3.setTma_activa(true);

            tipoMaquinaRepository.save(tipoMaquina1);
            tipoMaquinaRepository.save(tipoMaquina2);
            tipoMaquinaRepository.save(tipoMaquina3);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> crearTipoMaquina(@RequestBody TipoMaquinaDTO tipoMaquinaDTO) {
        try {
            // Realizar validación de los datos
            if (tipoMaquinaDTO.getTma_detalle().isEmpty()) {
                throw new IllegalArgumentException("Todos los datos de Tipo de Máquina son obligatorios.");
            }

            // Convertir DTO a entidad
            TipoMaquina tipoMaquina = TipoMaquinaDTO.toEntity(tipoMaquinaDTO);

            // Llamar al servicio para crear el Tipo de Máquina
            TipoMaquina nuevoTipoMaquina= tipoMaquinaService.createTipoMaquina(tipoMaquina);

            // Convertir entidad a DTO
            TipoMaquinaDTO nuevoTipoMaquinaDTO = TipoMaquinaDTO.fromEntity(nuevoTipoMaquina);

            // Mandar respuesta
            RespuestaDTO<TipoMaquinaDTO> respuesta = new RespuestaDTO<>(nuevoTipoMaquinaDTO, "TipoMaquina", "TipoMaquina creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo TipoMaquina: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear TipoMaquina: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<?> actualizarTipoMaquina(@PathVariable Integer id, @RequestBody TipoMaquinaDTO tipoMaquinaDTO) {
        try {
            // Lógica para modificar el Tipo de Máquina
            TipoMaquina tipoMaquinaExistente = tipoMaquinaService.buscartipoMaquinaPorId(id);

            if (tipoMaquinaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Tipo de Máquina con el ID proporcionado")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            //Modificar valores
            tipoMaquinaExistente.setTma_cod(tipoMaquinaDTO.getTma_cod());
            tipoMaquinaExistente.setTma_detalle(tipoMaquinaDTO.getTma_detalle());
            tipoMaquinaExistente.setTma_activa(tipoMaquinaDTO.isTma_activa());

            tipoMaquinaService.updateTipoMaquina(tipoMaquinaExistente);

            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("La modificación se ha realizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("404 NOT FOUND")
                    .message("Error al modificar el Tipo de Máquina. " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarTipoMaquina(@PathVariable Integer id) {
        try {
            TipoMaquina tipoMaquinaExistente = tipoMaquinaService.buscartipoMaquinaPorId(id);

            if (tipoMaquinaExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El ID que intenta eliminar no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

            } else {

                tipoMaquinaService.deleteTipoMaquinaById(id);
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("200 OK")
                        .message("Tipo de Máquina eliminado correctamente.")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorDTO);

            }

        } catch (DataAccessException e) { // Captura la excepción específica de acceso a datos
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al eliminar el Tipo de Máquina. " + e.getMessage())
                    .build();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorDTO.toString(), e);
        }
    }
}
