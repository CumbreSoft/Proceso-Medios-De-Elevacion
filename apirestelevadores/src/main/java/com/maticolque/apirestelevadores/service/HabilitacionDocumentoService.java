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

    //LISTAR
    public List<HabilitacionDocumento> getAllHabilitacionDocumento(){return habilitacionDocumentoRepository.findAll();}

    //BUSCAR POR ID
    public HabilitacionDocumento buscarHabilitacionDocumentoPorId(Integer id) {return habilitacionDocumentoRepository.findById(id).orElse(null);}

    //CREAR
    public HabilitacionDocumento createHabilitacionDocumento(HabilitacionDocumento habilitacionDocumento){return habilitacionDocumentoRepository.save(habilitacionDocumento);}

    //EDITAR
    public HabilitacionDocumento updateHabilitacionDocumento(HabilitacionDocumento habilitacionDocumento){return habilitacionDocumentoRepository.save(habilitacionDocumento);
    }

    //ELIMINAR
    public void deleteHabilitacionDocumentoById(Integer id){
        habilitacionDocumentoRepository.deleteById(id);
    }
}
