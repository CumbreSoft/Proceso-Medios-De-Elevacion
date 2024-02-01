package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.TipoAdjunto;
import com.maticolque.apirestelevadores.service.TipoAdjuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tipoAdjunto")
public class TipoAdjuntoController {

    @Autowired
    private TipoAdjuntoService tipoAdjuntoService;


    //GET
    @GetMapping
    public List<TipoAdjunto> listarTodo(){
        return tipoAdjuntoService.getAllTipoAdjunto();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public TipoAdjunto buscarTipoAdjuntoPorId(@PathVariable Integer id)
    {
        return tipoAdjuntoService.buscarTipoAdjuntoPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<TipoAdjunto> crearTipoAdjunto(@RequestBody TipoAdjunto tipoAdjunto){
        TipoAdjunto nuevoTipoAdjunto= tipoAdjuntoService.createTipoAdjunto(tipoAdjunto);
        return new RespuestaDTO<>(nuevoTipoAdjunto, "Tipo Adjunto creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarTipoAdjunto(@PathVariable Integer id, @RequestBody TipoAdjunto tipoAdjunto) {
        try {
            // Lógica para modificar el medio de elevación
            TipoAdjunto tipoAdjuntoExistente = tipoAdjuntoService.buscarTipoAdjuntoPorId(id);

            if (tipoAdjuntoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Tipo Adjunto con el ID proporcionado");
            }

            //Modificar valores
            tipoAdjuntoExistente.setTad_nombre(tipoAdjunto.getTad_nombre());
            tipoAdjuntoExistente.setTad_cod(tipoAdjunto.getTad_cod());
            tipoAdjuntoExistente.setTad_activo(tipoAdjunto.isTad_activo());

            // Agrega más propiedades según tu modelo
            tipoAdjuntoService.updateTipoAdjunto(tipoAdjuntoExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el medio de elevación");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarTipoAdjunto(@PathVariable Integer id){
        tipoAdjuntoService.deleteTipoAdjuntoById(id);
    }
}
