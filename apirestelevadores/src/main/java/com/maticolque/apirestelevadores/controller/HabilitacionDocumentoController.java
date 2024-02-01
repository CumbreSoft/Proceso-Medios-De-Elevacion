package com.maticolque.apirestelevadores.controller;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.service.HabilitacionDocumentoService;
import com.maticolque.apirestelevadores.service.TipoMaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/habilitacionDocumento")
public class HabilitacionDocumentoController {

    @Autowired
    private HabilitacionDocumentoService habilitacionDocumentoService;


    //GET
    @GetMapping
    public List<HabilitacionDocumento> listarTodo(){
        return habilitacionDocumentoService.getAllHabilitacionDocumento();
    }

    //GET POR ID
    @GetMapping("/{id}")
    public HabilitacionDocumento buscarHabilitacionDocumentoPorId(@PathVariable Integer id)
    {
        return habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);
    }

    //POST
    @PostMapping
    public RespuestaDTO<HabilitacionDocumento> crearHabilitacionDocumento(@RequestBody HabilitacionDocumento habilitacionDocumento){
        HabilitacionDocumento nuevaHabilitacionDocumento= habilitacionDocumentoService.createHabilitacionDocumento(habilitacionDocumento);
        return new RespuestaDTO<>(nuevaHabilitacionDocumento, "Habilitación Documento creado con éxito");
    }

    //PUT
    @PutMapping("editar/{id}")
    //@ResponseStatus(HttpStatus.OK) // Puedes usar esta anotación si solo quieres cambiar el código de estado HTTP
    public ResponseEntity<String> actualizarHabilitacionDocumento(@PathVariable Integer id, @RequestBody HabilitacionDocumento habilitacionDocumento) {
        try {
            // Lógica para modificar el medio de elevación
            HabilitacionDocumento habilitacionDocumentoExistente = habilitacionDocumentoService.buscarHabilitacionDocumentoPorId(id);

            if (habilitacionDocumentoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la Habilitación Documento con el ID proporcionado");
            }

            //Modificar valores
            habilitacionDocumentoExistente.setEmpresaHabilitacion(habilitacionDocumento.getEmpresaHabilitacion());
            habilitacionDocumentoExistente.setTipoAdjunto(habilitacionDocumento.getTipoAdjunto());
            habilitacionDocumentoExistente.setHdo_adjunto_orden(habilitacionDocumento.getHdo_adjunto_orden());
            habilitacionDocumentoExistente.setHdo_adjunto_fecha(habilitacionDocumento.getHdo_adjunto_fecha());
            habilitacionDocumentoExistente.setRevisor(habilitacionDocumento.getRevisor());

            // Agrega más propiedades según tu modelo
            habilitacionDocumentoService.updateHabilitacionDocumento(habilitacionDocumentoExistente);

            return ResponseEntity.ok("La modificación se ha realizado correctamente");

        } catch (Exception e) {
            // Manejar otras excepciones no específicas y devolver un código y mensaje genéricos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Se produjo un error al intentar modificar la Habilitación Documento");
        }
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public void elimimarHabilitacionDocumento(@PathVariable Integer id){
        habilitacionDocumentoService.deleteHabilitacionDocumentoById(id);
    }
}
