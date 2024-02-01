package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.service.RevisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/revisor")
public class RevisorController {

    @Autowired
    private RevisorService revisorService;


    //GET
    @GetMapping
    public List<Revisor> listarTodo(){
        return revisorService.getAllRevisor();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public Revisor buscarRevisorPorId(@PathVariable Integer id)
    {
        return revisorService.buscarRevisorPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Revisor> crearRevisoor(@RequestBody Revisor revisor){
        Revisor nuevoRevisor= revisorService.createRevisor(revisor);
        return new RespuestaDTO<>(nuevoRevisor, "Revisor creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarRevisor(@PathVariable Integer id, @RequestBody Revisor revisor) {
        try {
            // Lógica para modificar el medio de elevación
            Revisor revisorExistente = revisorService.buscarRevisorPorId(id);

            if (revisorExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Revisor con el ID proporcionado");
            }


            //Modificar valores
            revisorExistente.setRev_nombre(revisor.getRev_nombre());
            revisorExistente.setRev_numdoc(revisor.getRev_numdoc());
            revisorExistente.setRev_renov_mde(revisor.isRev_renov_mde());
            revisorExistente.setRev_renov_mde(revisor.isRev_renov_mde());
            revisorExistente.setRev_aprob_emp(revisor.isRev_aprob_emp());
            revisorExistente.setRev_activo(revisor.isRev_activo());

            // Agrega más propiedades según tu modelo
            revisorService.updateRevisor(revisorExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar al Revisor");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarRevisor(@PathVariable Integer id){
        revisorService.deleteRevisorById(id);
    }
}
