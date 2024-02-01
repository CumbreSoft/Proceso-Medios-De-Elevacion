package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.service.EmpresaPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/empresaPersona")
public class EmpresaPersonaController {

    @Autowired
    private EmpresaPersonaService empresaPersonaService;


    //GET
    @GetMapping
    public List<EmpresaPersona> listarTodo(){
        return empresaPersonaService.getAllEmpresaPersona();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public EmpresaPersona buscarEmpresaPersonaPorId(@PathVariable Integer id)
    {
        return empresaPersonaService.buscarEmpresaPersonaPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<EmpresaPersona> crearEmpresaPersona(@RequestBody EmpresaPersona empresaPersona){
        EmpresaPersona nuevaEmpresaPersona= empresaPersonaService.createEmpresaPersona(empresaPersona);
        return new RespuestaDTO<>(nuevaEmpresaPersona, "Tipo Máquina creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarEmpresaPersona(@PathVariable Integer id, @RequestBody EmpresaPersona empresaPersona) {
        try {
            // Lógica para modificar el medio de elevación
            EmpresaPersona empresaPersonaExistente = empresaPersonaService.buscarEmpresaPersonaPorId(id);

            if (empresaPersonaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la Empresa Persona con el ID proporcionado");
            }

            //Modificar valores
            empresaPersonaExistente.setEpe_es_dueno_emp(empresaPersona.isEpe_es_dueno_emp());
            empresaPersonaExistente.setEpe_es_reptec_emp(empresaPersona.isEpe_es_reptec_emp());
            empresaPersonaExistente.setEmpresa(empresaPersona.getEmpresa());
            empresaPersonaExistente.setPersona(empresaPersona.getPersona());

            // Agrega más propiedades según tu modelo
            empresaPersonaService.updateEmpresaPersona(empresaPersonaExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la Empresa Persona");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarEmpresaPersona(@PathVariable Integer id){
        empresaPersonaService.deleteEmpresaPersonaById(id);
    }
}
