package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.service.InmuebleMedioElevacionService;
import com.maticolque.apirestelevadores.service.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inmueblesMDE")
public class InmuebleMedioElevacionController {

    @Autowired
    private InmuebleMedioElevacionService inmuebleMedioElevacionService;


    //GET
    @GetMapping
    public List<InmuebleMedioElevacion> listarTodo(){
        return inmuebleMedioElevacionService.getAllInmueblesMDE();
    }


    //GET POR ID
    @GetMapping("/{id}")
    public InmuebleMedioElevacion buscarInmuebleMDEPorId(@PathVariable Integer id)
    {
        return inmuebleMedioElevacionService.buscarInmuebleMDEPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<InmuebleMedioElevacion> crearInmubleMDE(@RequestBody InmuebleMedioElevacion inmuebleMedioElevacion){
        InmuebleMedioElevacion nuevoInmuebleMedioElevacion= inmuebleMedioElevacionService.createInmuebleMDE(inmuebleMedioElevacion);
        return new RespuestaDTO<>(nuevoInmuebleMedioElevacion, "Inmueble Medio Elevación creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> modificarInmubleMDE(@RequestBody InmuebleMedioElevacion inmuebleMedioElevacion) {
        try {
            // Lógica para modificar la persona
            inmuebleMedioElevacionService.updateInmuebleMDE(inmuebleMedioElevacion);
            return ResponseEntity.ok("La modificación se ha realizado correctamente");
        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la persona");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarMDE(@PathVariable Integer id){
        inmuebleMedioElevacionService.deleteInmuebleMDEById(id);
    }
}
