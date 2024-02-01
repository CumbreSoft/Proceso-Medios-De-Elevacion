package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.service.DestinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/destino")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;


    //GET
    @GetMapping
    public List<Destino> listarTodo(){
        return destinoService.getAllDestino();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public Destino buscarDestinoPorId(@PathVariable Integer id)
    {
        return destinoService.buscarDestinoPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Destino> crearMedioElevacion(@RequestBody Destino destino){
        //return destinoService.createDestino(destino);
        Destino nuevoDestino = destinoService.createDestino(destino);
        return new RespuestaDTO<>(nuevoDestino, "Destino creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarDestino(@PathVariable Integer id, @RequestBody Destino destino) {
        try {
            // Lógica para modificar el medio de elevación
            Destino destinoExistente = destinoService.buscarDestinoPorId(id);

            if (destinoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Destino con el ID proporcionado");
            }

            //Modificar valores
            destinoExistente.setDst_cod(destino.getDst_cod());
            destinoExistente.setDst_detalle(destino.getDst_detalle());
            destinoExistente.setDst_activo(destino.isDst_activo());

            // Agrega más propiedades según tu modelo
            destinoService.updateDestino(destinoExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el Destino");
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarDestino(@PathVariable Integer id){
        destinoService.deleteDestinoById(id);
    }
}
