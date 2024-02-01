package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;


    //GET
    @GetMapping
    public List<Empresa> listarTodo(){
        return empresaService.getAllEmpresa();
    }


    //GET POR ID
    @GetMapping("/{id}")
    public Empresa buscarEmpresaPorId(@PathVariable Integer id)
    {
        return empresaService.buscarEmpresaPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<Empresa> crearEmpresa(@RequestBody Empresa empresa){
        Empresa nuevaEmpresa= empresaService.createEmpresa(empresa);
        return new RespuestaDTO<>(nuevaEmpresa, "Empresa creada con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarEmpresa(@PathVariable Integer id, @RequestBody Empresa empresa) {
        try {
            // Lógica para modificar el medio de elevación
            Empresa empresaExistente = empresaService.buscarEmpresaPorId(id);

            if (empresaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la Empresa con el ID proporcionado");
            }

            //Modificar valores
            empresaExistente.setEmp_razon(empresa.getEmp_razon());
            empresaExistente.setEmp_domic_legal(empresa.getEmp_domic_legal());
            empresaExistente.setEmp_cuit(empresa.getEmp_cuit());
            empresaExistente.setEmp_telefono(empresa.getEmp_telefono());
            empresaExistente.setEmp_correo(empresa.getEmp_correo());
            empresaExistente.setEmp_activa(empresa.isEmp_activa());

            // Agrega más propiedades según tu modelo
            empresaService.updateEmpresa(empresaExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la Empresa");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarEmpresa(@PathVariable Integer id){
        empresaService.deleteEmpresaById(id);
    }
}
