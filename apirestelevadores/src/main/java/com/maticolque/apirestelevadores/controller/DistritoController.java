package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.service.DistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/distrito")
public class DistritoController {
    @Autowired
    private DistritoService distritoService;


    //GET
    @GetMapping
    public List<Distrito> listarTodo(){
        return distritoService.getAllDistrito();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public Distrito buscarDestinoPorId(@PathVariable Integer id)
    {
        return distritoService.buscarDistritoPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Distrito> crearMedioElevacion(@RequestBody Distrito distrito){


        Distrito nuevoDistrito = distritoService.createDistrito(distrito);
        return new RespuestaDTO<>(nuevoDistrito, "Distrito creado con éxito");

    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarDestino(@PathVariable Integer id, @RequestBody Distrito distrito) {
        try {
            // Lógica para modificar el medio de elevación
            Distrito destinoExistente = distritoService.buscarDistritoPorId(id);

            if (destinoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Distrito con el ID proporcionado");
            }

            //Modificar valores
            destinoExistente.setDis_nombre(distrito.getDis_nombre());
            destinoExistente.setDis_activo(distrito.isDis_activo());
            destinoExistente.setDis_codigo(distrito.getDis_codigo());

            // Agrega más propiedades según tu modelo
            distritoService.updateDistrito(destinoExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el Distrito");
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarDestino(@PathVariable Integer id){
        distritoService.deleteDistritoById(id);
    }
}
