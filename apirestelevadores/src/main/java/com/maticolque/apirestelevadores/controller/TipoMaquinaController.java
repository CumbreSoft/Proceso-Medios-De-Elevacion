package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.model.TipoMaquina;
import com.maticolque.apirestelevadores.service.InmuebleService;
import com.maticolque.apirestelevadores.service.TipoMaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tipoMaquina")
public class TipoMaquinaController {

    @Autowired
    private TipoMaquinaService tipoMaquinaService;


    //GET
    @GetMapping
    public List<TipoMaquina> listarTodo(){
        return tipoMaquinaService.getAllTipoMaquina();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public TipoMaquina buscarTipoMaquinaPorId(@PathVariable Integer id)
    {
        return tipoMaquinaService.buscartipoMaquinaPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<TipoMaquina> crearTipoMaquina(@RequestBody TipoMaquina tipoMaquina){
        TipoMaquina nuevoTipoMaquina= tipoMaquinaService.createTipoMaquina(tipoMaquina);
        return new RespuestaDTO<>(nuevoTipoMaquina, "Tipo Máquina creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarTipoMaquina(@PathVariable Integer id, @RequestBody TipoMaquina tipoMaquina) {
        try {
            // Lógica para modificar el medio de elevación
            TipoMaquina tipoMaquinaExistente = tipoMaquinaService.buscartipoMaquinaPorId(id);

            if (tipoMaquinaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el medio de elevación con el ID proporcionado");
            }

            //Modificar valores
            tipoMaquinaExistente.setTma_cod(tipoMaquina.getTma_cod());
            tipoMaquinaExistente.setTma_detalle(tipoMaquina.getTma_detalle());
            tipoMaquinaExistente.setTma_activa(tipoMaquina.isTma_activa());

            // Agrega más propiedades según tu modelo
            tipoMaquinaService.updateTipoMaquina(tipoMaquinaExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el medio de elevación");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarTipoMaquina(@PathVariable Integer id){
        tipoMaquinaService.deleteTipoMaquinaById(id);
    }
}
