package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.repository.HabilitacionDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabilitacionDocumentoService {

    @Autowired
    private HabilitacionDocumentoRepository habilitacionDocumentoRepository;

    //Mostrar Habilitación Documento
    public List<HabilitacionDocumento> getAllHabilitacionDocumento(){
        return habilitacionDocumentoRepository.findAll();
    }

    //Mostrar por ID
    public HabilitacionDocumento buscarHabilitacionDocumentoPorId(Integer id)
    {
        return habilitacionDocumentoRepository.findById(id).orElse(null);
    }

    //Crear Habilitación Documento
    public HabilitacionDocumento createHabilitacionDocumento(HabilitacionDocumento habilitacionDocumento){
        return habilitacionDocumentoRepository.save(habilitacionDocumento);
    }

    //Editar Habilitación Documento
    public HabilitacionDocumento updateHabilitacionDocumento(HabilitacionDocumento habilitacionDocumento){
        return habilitacionDocumentoRepository.save(habilitacionDocumento);
    }

    //ELiminar Habilitación Documento
    public void deleteHabilitacionDocumentoById(Integer id){
        habilitacionDocumentoRepository.deleteById(id);
    }
}
