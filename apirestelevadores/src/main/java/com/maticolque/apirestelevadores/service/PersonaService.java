package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import com.maticolque.apirestelevadores.repository.InmueblePersonaRepository;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EmpresaPersonaRepository empresaPersonaRepository;

    @Autowired
    private InmueblePersonaRepository inmueblePersonaRepository;

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private InmueblePersonaService inmueblePersonaService;

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

    //Eliminar Persona
    public void deletePersonaById(Integer id){
        personaRepository.deleteById(id);
    }


    // *************** LOGICA PARA ELIMINAR UNA PERSONA ***************

    //Verificar si hay relacion de una Persona en EmpresaPersona
    public boolean verificarRelacionPersonaEnEP(Integer personaId) {
        return empresaPersonaService.verificarRelacionPersonaEnEP(personaId);
    }

    //Verificar si hay relacion de una Persona en InmueblePersona
    public boolean verificarRelacionPersonaEnIP(Integer personaId) {
        return inmueblePersonaService.verificarRelacionPersonaEnIP(personaId);
    }

    public String eliminarPersonaSiNoTieneRelaciones(Integer personaId) {
        Persona personaExistente = buscarPersonaPorId(personaId);
        if (personaExistente == null) {
            return "El ID proporcionado de la Persona no existe.";
        }

        if (verificarRelacionPersonaEnEP(personaId)) {
            return "La Persona tiene una relación con una Empresa (Empresa-Persona) y no puede ser eliminada.";
        }

        if (verificarRelacionPersonaEnIP(personaId)) {
            return "La Persona tiene una relación con un Inmueble (Inmueble-Persona) y no puede ser eliminada.";
        }

        deletePersonaById(personaId);
        return "Persona eliminada correctamente.";
    }
    // *************** LOGICA PARA ELIMINAR UNA PERSONA ***************




    public List<EmpresaPersona> obtenerTodosLosDatosDeEmpresaPersona() {
        return empresaPersonaRepository.findAll();
    }

    public List<InmueblePersona> obtenerTodosLosDatosDeInmueblePersona() {
        return inmueblePersonaRepository.findAll();
    }


    public List<EmpresaPersona> obtenerPrimeroLosDatosDeEmpresaPersona() {
        return empresaPersonaRepository.findAll();
    }

    public List<InmueblePersona> obtenerPrimeroLosDatosDeInmueblePersona() {
        return inmueblePersonaRepository.findAll();
    }
    //LISTAR EMPRESAS E INMUBLES DE LA PERSONA
    /*public List<Map<String, Object>> listarPersonasConEmpresasEInmuebles() {
        List<Persona> personas = personaRepository.findAll();
        List<Map<String, Object>> personasConEmpresasEInmuebles = new ArrayList<>();

        for (Persona persona : personas) {
            Map<String, Object> personaMap = new LinkedHashMap<>();
            personaMap.put("persona", persona);
            List<Empresa> empresas = new ArrayList<>();
            List<Inmueble> inmuebles = new ArrayList<>();

            for (EmpresaPersona empresaPersona : persona.getEmpresaPersonas()) {
                empresas.add(empresaPersona.getEmpresa());
            }

            for (InmueblePersona inmueblePersona : persona.getInmueblePersonas()) {
                inmuebles.add(inmueblePersona.getInmueble());
            }

            personaMap.put("empresas", empresas);
            personaMap.put("inmuebles", inmuebles);
            personasConEmpresasEInmuebles.add(personaMap);
        }

        return personasConEmpresasEInmuebles;
    }*/

}



