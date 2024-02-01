package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.MedioDocumento;
import com.maticolque.apirestelevadores.repository.MedioDocumetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioDocumentoService {

    @Autowired
    private MedioDocumetoRepository medioDocumetoRepository;

    //Mostrar Medio Documento
    public List<MedioDocumento> getAllMedioDocumento(){
        return medioDocumetoRepository.findAll();
    }

    //Mostrar por ID
    public MedioDocumento buscarMedioDocumentoPorId(Integer id)
    {
        return medioDocumetoRepository.findById(id).orElse(null);
    }

    //Crear Medio Documento
    public MedioDocumento createMedioDocumento(MedioDocumento medioDocumento){
        return medioDocumetoRepository.save(medioDocumento);
    }

    //Editar Medio Documento
    public MedioDocumento updateMedioDocumento(MedioDocumento medioDocumento){
        return medioDocumetoRepository.save(medioDocumento);
    }

    //ELiminar Medio Documento
    public void deleteMedioDocumentoById(Integer id){
        medioDocumetoRepository.deleteById(id);
    }
}
