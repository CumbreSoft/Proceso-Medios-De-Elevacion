package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    //Mostrar Persona
    public List<Persona> getAllPersona(){
        return personaRepository.findAll();
    }

    //Mostrar por ID
    public Persona buscarPersonaPorId(Integer id)
    {
        return personaRepository.findById(id).orElse(null);
    }

    //Crear Persona
    public Persona createPersona(Persona persona){
        return personaRepository.save(persona);
    }

    //Editar Persona
    public Persona updatePersona(Persona persona){
        return personaRepository.save(persona);
    }

    //ELiminar Persona
    public void deletePersonaById(Integer id){
        personaRepository.deleteById(id);
    }
}
