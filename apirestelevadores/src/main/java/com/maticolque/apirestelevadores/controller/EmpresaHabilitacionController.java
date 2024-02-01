package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.EmpresaHabilitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/empresaHabilitacion")
public class EmpresaHabilitacionController {

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;


    //GET
    @GetMapping
    public List<EmpresaHabilitacion> listarTodo(){
        return empresaHabilitacionService.getAllEmpresaHabilitacion();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public EmpresaHabilitacion buscarEmpresaHabilitacionPorId(@PathVariable Integer id)
    {
        return empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<EmpresaHabilitacion> crearEmpresaHabilitacion(@RequestBody EmpresaHabilitacion empresaHabilitacion){
        EmpresaHabilitacion nuevaEmpresaHabilitacion= empresaHabilitacionService.createEmpresaHabilitacion(empresaHabilitacion);
        return new RespuestaDTO<>(nuevaEmpresaHabilitacion, "Empresa Habilitación creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarEmpresaHabilitacion(@PathVariable Integer id, @RequestBody EmpresaHabilitacion empresaHabilitacion) {
        try {
            // Lógica para modificar el medio de elevación
            EmpresaHabilitacion empresaHabilitacionExistente = empresaHabilitacionService.buscarEmpresaHabilitacionPorId(id);

            if (empresaHabilitacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la Empresa Habilitación con el ID proporcionado");
            }

            //Modificar valores
            empresaHabilitacionExistente.setEha_fecha(empresaHabilitacion.getEha_fecha());
            empresaHabilitacionExistente.setEmpresa(empresaHabilitacion.getEmpresa());
            empresaHabilitacionExistente.setEha_expediente(empresaHabilitacion.getEha_expediente());
            empresaHabilitacionExistente.setEha_habilitada(empresaHabilitacion.isEha_habilitada());
            empresaHabilitacionExistente.setEha_vto_hab(empresaHabilitacion.getEha_vto_hab());
            empresaHabilitacionExistente.setRevisor(empresaHabilitacion.getRevisor());

            // Agrega más propiedades según tu modelo
            empresaHabilitacionService.updateEmpresaHabilitacion(empresaHabilitacionExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la Empresa Habilitación");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarEmpresaHabilitacion(@PathVariable Integer id){
        empresaHabilitacionService.deleteEmpresaHabilitacionById(id);
    }
}
