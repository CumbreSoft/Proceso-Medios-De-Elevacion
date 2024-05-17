package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<InmuebleMedioElevacion> obtenerPrimeroLosDatosDeInmuebleMedioDeElevacion() {
        return inmuebleMedioElevacionRepository.findAll();
    }

    // Nuevo método para filtrar los medios de elevación por ID de inmueble
    public List<InmuebleMedioElevacion> obtenerMediosDeElevacionPorInmuebleId(Integer inmuebleId) {
        List<InmuebleMedioElevacion> inmuebleMedioElevacionList = inmuebleMedioElevacionRepository.findAll();
        List<InmuebleMedioElevacion> mediosDeElevacionPorInmueble = new ArrayList<>();

        for (InmuebleMedioElevacion ime : inmuebleMedioElevacionList) {
            if (ime.getInmueble().getInm_id() == inmuebleId) {
                mediosDeElevacionPorInmueble.add(ime);
            }
        }

        return mediosDeElevacionPorInmueble;
    }
}
