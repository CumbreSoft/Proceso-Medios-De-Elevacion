package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioHabilitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/medioHabilitacion")
public class MedioHabilitacionController {

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;


    //GET
    @GetMapping
    public List<MedioHabilitacion> listarTodo(){
        return medioHabilitacionService.getAllMedioHabilitacion();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public MedioHabilitacion buscarMedioHabilitacionPorId(@PathVariable Integer id)
    {
        return medioHabilitacionService.buscarmedioHabilitacionPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<MedioHabilitacion> crearMedioHabilitacion(@RequestBody MedioHabilitacion medioHabilitacion){
        MedioHabilitacion nuevoMedioHabilitacion= medioHabilitacionService.createMedioHabilitacion(medioHabilitacion);
        return new RespuestaDTO<>(nuevoMedioHabilitacion, "Medio Habilitación creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarMedioHabilitacion(@PathVariable Integer id, @RequestBody MedioHabilitacion medioHabilitacion) {
        try {
            // Lógica para modificar el medio de elevación
            MedioHabilitacion medioHabilitacionExistente = medioHabilitacionService.buscarmedioHabilitacionPorId(id);

            if (medioHabilitacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Medio Habilitación con el ID proporcionado");
            }

            //Modificar valores
            medioHabilitacionExistente.setMha_fecha(medioHabilitacion.getMha_fecha());
            medioHabilitacionExistente.setEmpresa(medioHabilitacion.getEmpresa());
            medioHabilitacionExistente.setPersona(medioHabilitacion.getPersona());
            medioHabilitacionExistente.setMha_expediente(medioHabilitacion.getMha_expediente());
            medioHabilitacionExistente.setMha_fecha_vto(medioHabilitacion.getMha_fecha_vto());
            medioHabilitacionExistente.setMha_fecha_pago(medioHabilitacion.getMha_fecha_pago());
            medioHabilitacionExistente.setMha_fecha_inspec(medioHabilitacion.getMha_fecha_inspec());
            medioHabilitacionExistente.setMha_planos_aprob(medioHabilitacion.isMha_planos_aprob());
            medioHabilitacionExistente.setMha_habilitado(medioHabilitacion.isMha_habilitado());
            medioHabilitacionExistente.setMha_oblea_entregada(medioHabilitacion.isMha_oblea_entregada());
            medioHabilitacionExistente.setMha_vto_hab(medioHabilitacion.getMha_vto_hab());
            medioHabilitacionExistente.setRevisor(medioHabilitacion.getRevisor());

            // Agrega más propiedades según tu modelo
            medioHabilitacionService.updateMedioHabilitacion(medioHabilitacionExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el Medio Habilitación");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarMedioHabilitacion(@PathVariable Integer id){
        medioHabilitacionService.deleteMedioHabilitacionById(id);
    }
}
