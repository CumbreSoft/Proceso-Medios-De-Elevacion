package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.MedioDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/medioDocumento")
public class MedioDocumentoController {

    @Autowired
    private MedioDocumentoService medioDocumentoService;


    //GET
    @GetMapping
    public List<MedioDocumento> listarTodo(){
        return medioDocumentoService.getAllMedioDocumento();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public MedioDocumento buscarMedioDocumentoPorId(@PathVariable Integer id)
    {
        return medioDocumentoService.buscarMedioDocumentoPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<MedioDocumento> crearMedioDocumento(@RequestBody MedioDocumento medioDocumento){
        MedioDocumento nuevoMedioDocumento= medioDocumentoService.createMedioDocumento(medioDocumento);
        return new RespuestaDTO<>(nuevoMedioDocumento, "Medio Documento creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarMedioDocumento(@PathVariable Integer id, @RequestBody MedioDocumento medioDocumento) {
        try {
            // Lógica para modificar el medio de elevación
            MedioDocumento medioDocumentoExistente = medioDocumentoService.buscarMedioDocumentoPorId(id);

            if (medioDocumentoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el Medio Documento con el ID proporcionado");
            }

            //Modificar valores
            medioDocumentoExistente.setMedioHabilitacion(medioDocumento.getMedioHabilitacion());
            medioDocumentoExistente.setTipoAdjunto(medioDocumento.getTipoAdjunto());
            medioDocumentoExistente.setMdo_adjunto_orden(medioDocumento.getMdo_adjunto_orden());
            medioDocumentoExistente.setMdo_adjunto_fecha(medioDocumento.getMdo_adjunto_fecha());
            medioDocumentoExistente.setRevisor(medioDocumento.getRevisor());

            // Agrega más propiedades según tu modelo
            medioDocumentoService.updateMedioDocumento(medioDocumentoExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar el Medio Documento");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarMedioDocumento(@PathVariable Integer id){
        medioDocumentoService.deleteMedioDocumentoById(id);
    }
}
