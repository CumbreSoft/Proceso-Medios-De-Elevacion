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

    //LISTAR
    public List<MedioHabilitacion> getAllMedioHabilitacion(){
        return medioHabilitacionRepository.findAll();
    }

    //BUSCAR POR ID
    public MedioHabilitacion buscarmedioHabilitacionPorId(Integer id) {return medioHabilitacionRepository.findById(id).orElse(null);}

    //CREAR
    public MedioHabilitacion createMedioHabilitacion(MedioHabilitacion medioHabilitacion){return medioHabilitacionRepository.save(medioHabilitacion);}

    //EDITAR
    public MedioHabilitacion updateMedioHabilitacion(MedioHabilitacion medioHabilitacion){return medioHabilitacionRepository.save(medioHabilitacion);}

    //ELIMINAR
    public void deleteMedioHabilitacionById(Integer id){
        medioHabilitacionRepository.deleteById(id);
    }
}
