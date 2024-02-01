package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.repository.InmuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    //Mostrar inmueble
    public List<Inmueble> getAllInmuebles(){
        return inmuebleRepository.findAll();
    }

    //Mostrar por ID
    public Inmueble buscarInmbublePorId(Integer id)
    {
        return inmuebleRepository.findById(id).orElse(null);
    }

    //Crear inmueble
    public Inmueble createInmueble(Inmueble inmuble){
        return inmuebleRepository.save(inmuble);
    }

    //Editar inmueble
    public Inmueble updateInmueble(Inmueble inmueble){
        return inmuebleRepository.save(inmueble);
    }

    //ELiminar inmueble
    public void deleteInmuebleById(Integer id){
        inmuebleRepository.deleteById(id);
    }
}
