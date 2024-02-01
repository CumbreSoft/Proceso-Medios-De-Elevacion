package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.repository.InmueblePersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmueblePersonaService {

    @Autowired
    private InmueblePersonaRepository inmueblePersonaRepository;

    //Mostrar Inmueble Persona
    public List<InmueblePersona> getAllInmueblePersona(){
        return inmueblePersonaRepository.findAll();
    }

    //Mostrar por ID
    public InmueblePersona buscarInmueblePersonaPorId(Integer id)
    {
        return inmueblePersonaRepository.findById(id).orElse(null);
    }

    //Crear Inmueble Persona
    public InmueblePersona createInmueblePersona(InmueblePersona inmueblePersona){
        return inmueblePersonaRepository.save(inmueblePersona);
    }

    //Editar Inmueble Persona
    public InmueblePersona updateInmueblePersona(InmueblePersona inmueblePersona){
        return inmueblePersonaRepository.save(inmueblePersona);
    }

    //ELiminar Inmueble Persona
    public void deleteInmueblePersonaById(Integer id){
        inmueblePersonaRepository.deleteById(id);
    }
}
