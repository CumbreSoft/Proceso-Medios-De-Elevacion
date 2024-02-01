package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.repository.MedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioElevacionService {

    @Autowired
    private MedioElevacionRepository medioElevacionRepository;

    //Mostrar Medio Elevacion
    public List<MedioElevacion> getAllMedioElevacion(){
        return medioElevacionRepository.findAll();
    }

    //Mostrar por ID
    public MedioElevacion buscarMedioElevacionPorId(Integer id)
    {
        return medioElevacionRepository.findById(id).orElse(null);
    }

    //Crear Medio Elevacion
    public MedioElevacion createMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //Editar Medio Elevacion
    public MedioElevacion updateMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //ELiminar Medio Elevacion
    public void deleteMedioElevacionById(Integer id){
        medioElevacionRepository.deleteById(id);
    }
}
