package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.service.InmueblePersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inmueblePersona")
public class InmueblePersonaController {

    @Autowired
    private InmueblePersonaService inmueblePersonaService;

    //GET
    @GetMapping
    public List<InmueblePersona> listarTodo(){
        return inmueblePersonaService.getAllInmueblePersona();
    }


    //GET POR ID
    @GetMapping("/{id}")
    public InmueblePersona buscarInmueblePersona(@PathVariable Integer id)
    {
        return inmueblePersonaService.buscarInmueblePersonaPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<InmueblePersona> crearInmublePersona(@RequestBody InmueblePersona inmueblePersona){
        InmueblePersona nuevoInmueblePersona= inmueblePersonaService.createInmueblePersona(inmueblePersona);
        return new RespuestaDTO<>(nuevoInmueblePersona, "Inmueble Persona creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarInmueblePersona(@PathVariable Integer id, @RequestBody InmueblePersona inmueblePersona) {
        try {
            // Lógica para modificar el medio de elevación
            InmueblePersona inmueblePersonaExistente = inmueblePersonaService.buscarInmueblePersonaPorId(id);

            if (inmueblePersonaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Inmuble Persona con el ID proporcionado");
            }

            //Modificar valores
            inmueblePersonaExistente.setIpe_es_admin_edif(inmueblePersona.isIpe_es_admin_edif());
            inmueblePersonaExistente.setIpe_es_coprop_edif(inmueblePersona.isIpe_es_coprop_edif());
            inmueblePersonaExistente.setInmueble(inmueblePersona.getInmueble());
            inmueblePersonaExistente.setPersona(inmueblePersona.getPersona());

            // Agrega más propiedades según tu modelo
            inmueblePersonaService.updateInmueblePersona(inmueblePersonaExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el Inmuble Persona");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarInmueblePersona(@PathVariable Integer id){
        inmueblePersonaService.deleteInmueblePersonaById(id);
    }
}
