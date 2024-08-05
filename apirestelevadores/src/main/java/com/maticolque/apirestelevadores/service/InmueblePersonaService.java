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

    //LISTAR
    public List<InmueblePersona> getAllInmueblePersona(){
        return inmueblePersonaRepository.findAll();
    }

    //BUSCAR POR ID
    public InmueblePersona buscarInmueblePersonaPorId(Integer id) {return inmueblePersonaRepository.findById(id).orElse(null);}

    //CREAR
    public InmueblePersona createInmueblePersona(InmueblePersona inmueblePersona){return inmueblePersonaRepository.save(inmueblePersona);}

    //EDITAR
    public InmueblePersona updateInmueblePersona(InmueblePersona inmueblePersona){return inmueblePersonaRepository.save(inmueblePersona);}

    //ELIMINAR
    public void deleteInmueblePersonaById(Integer id){
        inmueblePersonaRepository.deleteById(id);
    }
}
