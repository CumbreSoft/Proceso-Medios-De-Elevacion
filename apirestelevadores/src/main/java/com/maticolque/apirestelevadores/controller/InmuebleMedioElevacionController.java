package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.*;
import com.maticolque.apirestelevadores.dtosimple.EmpresaPersonaSimpleDTO;
import com.maticolque.apirestelevadores.dtosimple.MedioElevacionSimpleDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import com.maticolque.apirestelevadores.service.InmuebleMedioElevacionService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

            // Obtener la lista de Inmueble-MDE
            List<InmuebleMedioElevacion> inmuebleMDE = inmuebleMedioElevacionService.getAllInmueblesMDE();

            // Verificar la lista de Inmueble-MDE
            if (inmuebleMDE.isEmpty()) {
                // Crear instancia de ErrorDTO con el código de error y el mensaje
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("La base de datos está vacía, no se encontraron Inmueble-MDE.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Convertir la entidad en un DTO
            List<InmuebleMedioElevacionReadDTO> inmuebleMDEDTO = new ArrayList<>();
            for (InmuebleMedioElevacion inmuebleMedioElevacion : inmuebleMDE) {
                InmuebleMedioElevacionReadDTO readDTO = InmuebleMedioElevacionReadDTO.fromEntity(inmuebleMedioElevacion);
                inmuebleMDEDTO.add(readDTO);
            }

            // Crear mapa para estructurar la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("inmueblesMDE", inmuebleMDEDTO);

            // Retornar la respuesta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Crear instancia de ErrorDTO con el código de error y el mensaje
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Inmueble-MDE: " + e.getMessage())
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
    public ResponseEntity<?> crearInmuebleMDE(@RequestBody InmuebleMedioElevacionCreateDTO createDTO) {
        try {
            // Validar datos
            if (createDTO.getIme_inm_id() == 0) {
                throw new IllegalArgumentException("El Inmueble es obligatorio.");
            } else if (createDTO.getIme_mde_id() == 0) {
                throw new IllegalArgumentException("El Medio de Elevación es obligatorio.");
            }

            // Buscar Inmueble y MDE por sus IDs
            Inmueble inmueble = inmuebleService.buscarInmueblePorId(createDTO.getIme_inm_id());
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(createDTO.getIme_mde_id());

            // Verificar si los IDs existen
            if (inmueble == null || medioElevacion == null) {
                throw new IllegalArgumentException("ID de Inmueble o MDE no encontrado.");
            }

            // Convertir DTO a entidad
            InmuebleMedioElevacion inmuebleMedioElevacion = InmuebleMedioElevacionCreateDTO.toEntity(createDTO, inmueble, medioElevacion);

            // Crear relacion Inmueble-MDE
            InmuebleMedioElevacion nuevoInmuebleMDE = inmuebleMedioElevacionService.createInmuebleMDE(inmuebleMedioElevacion);

            // Convertir entidad a DTO
            InmuebleMedioElevacionReadDTO nuevoInmuebleMedioElevacionReadDTO = InmuebleMedioElevacionReadDTO.fromEntity(nuevoInmuebleMDE);

            // Mandar respuesta
            RespuestaDTO<InmuebleMedioElevacionReadDTO> respuesta = new RespuestaDTO<>(nuevoInmuebleMedioElevacionReadDTO, "Inmueble-MDE", "Inmueble-MDE creado con éxito.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            // Capturar excepción de validación
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "Error al crear un nuevo Inmueble-MDE: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            ErrorDTO errorDTO = new ErrorDTO("500 INTERNAL SERVER ERROR", "Error al crear un nuevo Inmueble-MDE: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //PUT
    @PutMapping("/editarInmuebleDeMedioElevacion/{id}")
    public ResponseEntity<?> actualizarInmuebleDeMedioElevacion(@PathVariable Integer id, @RequestBody InmuebleMedioElevacionUpdateDTO updateDTO) {
        try {
            // Obtener ID de Inmueble-MDE
            InmuebleMedioElevacion inmuebleMedioElevacionExistente = inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);

            // Verificar si existe el ID de Inmueble-MDE
            if (inmuebleMedioElevacionExistente == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Inmueble-MDE con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener ID del nuevo Inmueble
            Inmueble nuevoInmueble = inmuebleService.buscarInmueblePorId(updateDTO.getIme_inm_id());

            // Verificar si existe el ID del nuevo Inmueble
            if (nuevoInmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró el Inmueble con el ID proporcionado.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Actualizar los campos de Inmueble-MDE
            inmuebleMedioElevacionExistente.setInmueble(nuevoInmueble);

            // Actualizar Inmueble-MDE
            inmuebleMedioElevacionService.updateInmuebleMDE(inmuebleMedioElevacionExistente);

            //Mandar respuesta
            ErrorDTO successDTO = ErrorDTO.builder()
                    .code("200 OK")
                    .message("El Inmueble-MDE se ha actualizado correctamente.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(successDTO);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("500 INTERNAL SERVER ERROR")
                    .message("Error al actualizar el Inmueble-MDE: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //CON ESTA API OBTENGO POR NUMERO DE PADRON EL O LOS MEDIOS DE ELEVACION CON SU MAQUINA, UBICACION Y EMPRESA CON SU LISTA DE PERSONAS
    @GetMapping("/padron/{inmPadron}/mediosElevacion")
    public ResponseEntity<?> obtenerMediosElevacionPorPadron(@PathVariable("inmPadron") Integer inmPadron) {
        try {

            // Obtener el Inmueble por su padron
            Inmueble inmueble = inmuebleService.buscarInmueblePorPadron(inmPadron);

            // Verificar si el ID existe
            if (inmueble == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontró un inmueble con el padron: " + inmPadron)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener todos los datos de InmuebleMedioElevacion asociados al inmueble
            List<InmuebleMedioElevacion> inmuebleMedioElevacionList = inmuebleMedioElevacionService.obtenerMediosDeElevacionPorInmuebleId(inmueble.getInm_id());

            // Verificar si no hay medios de elevación asociados
            if (inmuebleMedioElevacionList.isEmpty()) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("No se encontraron medios de elevación para el inmueble con el padron: " + inmPadron)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Lista para almacenar los medios de elevación asociados al inmueble
            List<MedioElevacionSimpleDTO> medioElevacionSimpleDTO = new ArrayList<>();

            for (InmuebleMedioElevacion imde : inmuebleMedioElevacionList) {
                EmpresaPersonaSimpleDTO empresaDTO = null;

                // Obtener la empresa asociada al medio de elevación
                Empresa empresa = imde.getMedioElevacion().getEmpresa();
                if (empresa != null) {
                    // Obtener la lista de personas asociadas a la empresa
                    List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresa.getEmp_id());
                    empresaDTO = EmpresaPersonaSimpleDTO.fromEntity(empresa, personas);
                }

                // Mapear los datos del medio de elevación a un DTO
                MedioElevacionSimpleDTO medioElevacionDTO = MedioElevacionSimpleDTO.fromEntity(imde.getMedioElevacion(), empresaDTO);
                medioElevacionSimpleDTO.add(medioElevacionDTO);
            }

            //Retornar la respuesta
            return ResponseEntity.ok(Collections.singletonMap("mediosElevacion", medioElevacionSimpleDTO));

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios de Elevación para el Inmueble con padrón: " + inmPadron + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //CON ESTA API OBTENGO POR ID MEDIO DE ELEVACION, SU MAQUINA, UBICACION Y EMPRESA CON SU LISTA DE PERSONAS
    @GetMapping("/medioElevacion/{medioElevacionId}")
    public ResponseEntity<?> obtenerMedioElevacionPorId(@PathVariable("medioElevacionId") Integer medioElevacionId) {
        try {

            // Obtener el Medio De Elevación por su ID
            MedioElevacion medioElevacion = medioElevacionService.buscarMedioElevacionPorId(medioElevacionId);

            // Verificar si el ID existe
            if (medioElevacion == null) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .code("404 NOT FOUND")
                        .message("El Medio de Elevación con ID: " + medioElevacionId + " no existe.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            }

            // Obtener la empresa asociada al medio de elevación
            EmpresaPersonaSimpleDTO empresaPersonaSimpleDTO = null;
            Empresa empresa = medioElevacion.getEmpresa();
            if (empresa != null) {
                // Obtener la lista de personas asociadas a la empresa
                List<Persona> personas = empresaPersonaService.obtenerPersonasPorEmpresa(empresa.getEmp_id());
                empresaPersonaSimpleDTO = EmpresaPersonaSimpleDTO.fromEntity(empresa, personas);
            }

            // Mapear los datos del Medio De Elevación a un DTO
            MedioElevacionSimpleDTO medioElevacionSimpleDTO = MedioElevacionSimpleDTO.fromEntity(medioElevacion, empresaPersonaSimpleDTO);

            //Retornar la respuesta
            return ResponseEntity.ok(Collections.singletonMap("medioElevacion", medioElevacionSimpleDTO));

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener el Medio de Elevación con ID: " + medioElevacionId + ": " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }

    //CON ESTA API OBTENGO EL PRIMER INMUEBLE RELACIONADO CON UN MEDIO DE ELEVACION
    @GetMapping("/primerInmuebleMedioElevacion")
    public ResponseEntity<?> obtenerPrimerInmuebleMedioElevacion() {
        try {
            // Obtener la primera relación de cada inmueble
            List<InmuebleMedioElevacion> inmuebleMedioElevacionList = inmuebleMedioElevacionService.obtenerPrimerInmuebleMedioElevacion();

            // Preparar la respuesta usando los DTOs
            List<InmuebleMedioElevacionReadDTO> inmuebleMedioElevacionDTOList = inmuebleMedioElevacionList.stream()
                    .map(InmuebleMedioElevacionReadDTO::fromEntity)
                    .collect(Collectors.toList());

            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("medioElevacionesInmueble", inmuebleMedioElevacionDTOList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("ERR_INTERNAL_SERVER_ERROR")
                    .message("Error al obtener la lista de Medios de Elevación con su primer Inmueble: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
        }
    }
}