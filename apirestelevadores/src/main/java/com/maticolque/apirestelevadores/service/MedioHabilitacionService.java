package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import com.maticolque.apirestelevadores.model.MedioHabilitacion;
import com.maticolque.apirestelevadores.repository.MedioHabilitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioHabilitacionService {

    @Autowired
    private MedioHabilitacionRepository medioHabilitacionRepository;

    //Mostrar Medio Habilitacion
    public List<MedioHabilitacion> getAllMedioHabilitacion(){
        return medioHabilitacionRepository.findAll();
    }

    //Mostrar por ID
    public MedioHabilitacion buscarmedioHabilitacionPorId(Integer id)
    {
        return medioHabilitacionRepository.findById(id).orElse(null);
    }

    //Crear Medio Habilitacion
    public MedioHabilitacion createMedioHabilitacion(MedioHabilitacion medioHabilitacion){
        return medioHabilitacionRepository.save(medioHabilitacion);
    }

    //Editar Medio Habilitacion
    public MedioHabilitacion updateMedioHabilitacion(MedioHabilitacion medioHabilitacion){
        return medioHabilitacionRepository.save(medioHabilitacion);
    }

    //Eliminar Medio Habilitacion
    public void deleteMedioHabilitacionById(Integer id){
        medioHabilitacionRepository.deleteById(id);
    }
}
