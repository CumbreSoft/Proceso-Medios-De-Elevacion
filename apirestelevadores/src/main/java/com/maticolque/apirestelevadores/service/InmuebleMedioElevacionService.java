package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmuebleMedioElevacionService {

    @Autowired
    private InmuebleMedioElevacionRepository inmuebleMedioElevacionRepository;

    //Mostrar inmueble
    public List<InmuebleMedioElevacion> getAllInmueblesMDE(){
        return inmuebleMedioElevacionRepository.findAll();
    }

    //Mostrar por ID
    public InmuebleMedioElevacion buscarInmuebleMDEPorId(Integer id)
    {
        return inmuebleMedioElevacionRepository.findById(id).orElse(null);
    }

    //Crear inmueble
    public InmuebleMedioElevacion createInmuebleMDE(InmuebleMedioElevacion inmuebleMedioElevacion){
        return inmuebleMedioElevacionRepository.save(inmuebleMedioElevacion);
    }

    //Editar inmueble
    public InmuebleMedioElevacion updateInmuebleMDE(InmuebleMedioElevacion inmuebleMedioElevacion){
        return inmuebleMedioElevacionRepository.save(inmuebleMedioElevacion);
    }

    //ELiminar inmueble
    public void deleteInmuebleMDEById(Integer id){
        inmuebleMedioElevacionRepository.deleteById(id);
    }
}