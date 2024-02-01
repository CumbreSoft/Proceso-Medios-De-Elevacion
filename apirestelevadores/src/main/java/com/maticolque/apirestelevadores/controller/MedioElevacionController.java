package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.service.MedioElevacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/medioElevacion")
public class MedioElevacionController {

    @Autowired
    private MedioElevacionService medioElevacionService;


    //GET
    @GetMapping
    public List<MedioElevacion> listarTodo(){
        return medioElevacionService.getAllMedioElevacion();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public MedioElevacion buscarMedioElevacionPorId(@PathVariable Integer id)
    {
        return medioElevacionService.buscarMedioElevacionPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<MedioElevacion> crearMedioElevacion(@RequestBody MedioElevacion medioElevacion){
        //return medioElevacionService.createMedioElevacion(medioElevacion);
        MedioElevacion nuevoMedioElevacion = medioElevacionService.createMedioElevacion(medioElevacion);
        return new RespuestaDTO<>(nuevoMedioElevacion, "Medio Elevación creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarMedioElevacion(@PathVariable Integer id, @RequestBody MedioElevacion medioElevacion) {
        try {
            // Lógica para modificar el medio de elevación
            MedioElevacion medioElevacionExistente = medioElevacionService.buscarMedioElevacionPorId(id);

            if (medioElevacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el medio de elevación con el ID proporcionado");
            }

            //Modificar valores
            medioElevacionExistente.setMde_ubicacion(medioElevacion.getMde_ubicacion());
            medioElevacionExistente.setMde_tipo(medioElevacion.getMde_tipo());
            medioElevacionExistente.setMde_niveles(medioElevacion.getMde_niveles());
            medioElevacionExistente.setMde_activo(medioElevacion.isMde_activo());
            medioElevacionExistente.setTiposMaquinas(medioElevacion.getTiposMaquinas());

            // Agrega más propiedades según tu modelo
            medioElevacionService.updateMedioElevacion(medioElevacionExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el medio de elevación");
        }
    }


    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarMedioElevacion(@PathVariable Integer id){
        medioElevacionService.deleteMedioElevacionById(id);
    }

}