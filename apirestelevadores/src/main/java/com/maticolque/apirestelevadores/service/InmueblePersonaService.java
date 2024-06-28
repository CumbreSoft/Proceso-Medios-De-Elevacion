package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
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

    //Eliminar Inmueble Persona
    public void deleteInmueblePersonaById(Integer id){
        inmueblePersonaRepository.deleteById(id);
    }


    //Metodo para buscar si una Persona tiene una relacion con un Inmueble (InmueblePersona)
    public boolean verificarRelacionPersonaEnIP(Integer personaId) {
        List<InmueblePersona> relaciones = inmueblePersonaRepository.findAll();
        return relaciones.stream().anyMatch(p -> p.getPersona().getPer_id() == personaId);
    }

    //Metodo para buscar si un Inmueble tiene una relacion con una Persona (InmueblePersona)
    public boolean verificarRelacionInmuebleEnIP(Integer inmuebleId) {
        List<InmueblePersona> relaciones = inmueblePersonaRepository.findAll();
        return relaciones.stream().anyMatch(i -> i.getInmueble().getInm_id() == inmuebleId);
    }


}
