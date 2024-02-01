package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/persona")
public class PersonaController {

    @Autowired
    private PersonaService personaService;


    //GET
    @GetMapping
    public List<Persona> listarTodo(){
        return personaService.getAllPersona();
    }


    //GET POR ID
    @GetMapping("/{id}")
    public Persona buscarPersonaPorId(@PathVariable Integer id)
    {
        return personaService.buscarPersonaPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Persona> crearPersona(@RequestBody Persona persona){
        Persona nuevaPersona= personaService.createPersona(persona);
        return new RespuestaDTO<>(nuevaPersona, "Persona creada con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarPersona(@PathVariable Integer id, @RequestBody Persona persona) {
        try {
            // Lógica para modificar el medio de elevación
            Persona personaExistente = personaService.buscarPersonaPorId(id);

            if (personaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la Persona con el ID proporcionado");
            }

            //Modificar valores
            personaExistente.setPer_nombre(persona.getPer_nombre());
            personaExistente.setPer_tipodoc(persona.getPer_tipodoc());
            personaExistente.setPer_numdoc(persona.getPer_numdoc());
            personaExistente.setPer_telefono(persona.getPer_telefono());
            personaExistente.setPer_correo(persona.getPer_correo());
            personaExistente.setPer_es_dueno_emp(persona.isPer_es_dueno_emp());
            personaExistente.setPer_es_reptec_emp(persona.isPer_es_reptec_emp());
            personaExistente.setPer_es_admin_edif(persona.isPer_es_admin_edif());
            personaExistente.setPer_es_coprop_edif(persona.isPer_es_coprop_edif());
            personaExistente.setPer_activa(persona.isPer_activa());

            // Agrega más propiedades según tu modelo
            personaService.updatePersona(personaExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la Persona");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarPersona(@PathVariable Integer id){
        personaService.deletePersonaById(id);
    }
}
