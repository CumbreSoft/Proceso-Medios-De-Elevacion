package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.service.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inmuebles")
public class InmuebleController {

    @Autowired
    private InmuebleService inmuebleService;


    //GET
    @GetMapping
    public List<Inmueble> listarTodo(){
        return inmuebleService.getAllInmuebles();
    }


    //GET POR ID
    @GetMapping("/{id}")
    public Inmueble buscarInmueblePorId(@PathVariable Integer id)
    {
        return inmuebleService.buscarInmbublePorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Inmueble> crearInmuble(@RequestBody Inmueble inmueble){

        Inmueble nuevoInmueble = inmuebleService.createInmueble(inmueble);
        return new RespuestaDTO<>(nuevoInmueble, "Inmueble creado con éxito");

    }


    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> modificarInmuble(@RequestBody Inmueble inmueble) {
        try {
            // Lógica para modificar la persona
            inmuebleService.updateInmueble(inmueble);
            return ResponseEntity.ok("La modificación se ha realizado correctamente");
        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la persona");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarInmuble(@PathVariable Integer id){
        inmuebleService.deleteInmuebleById(id);
    }

}