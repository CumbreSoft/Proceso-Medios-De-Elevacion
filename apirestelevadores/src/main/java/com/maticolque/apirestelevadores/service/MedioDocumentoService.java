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

    //LISTAR
    public List<MedioDocumento> getAllMedioDocumento(){
        return medioDocumetoRepository.findAll();
    }

    //BUSCAR POR ID
    public MedioDocumento buscarMedioDocumentoPorId(Integer id) {return medioDocumetoRepository.findById(id).orElse(null);}

    //CREAR
    public MedioDocumento createMedioDocumento(MedioDocumento medioDocumento){return medioDocumetoRepository.save(medioDocumento);}

    //EDITAR
    public MedioDocumento updateMedioDocumento(MedioDocumento medioDocumento){return medioDocumetoRepository.save(medioDocumento);}

    //ELIMINAR
    public void deleteMedioDocumentoById(Integer id){
        medioDocumetoRepository.deleteById(id);
    }
}
