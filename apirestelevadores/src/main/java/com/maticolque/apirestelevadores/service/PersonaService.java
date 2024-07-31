package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import com.maticolque.apirestelevadores.repository.InmueblePersonaRepository;
import com.maticolque.apirestelevadores.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorDTO> eliminarPersonaSiNoTieneRelaciones(int personaId) {
        // Buscar persona
        Optional<Persona> persona = personaRepository.findById(personaId);
        if (!persona.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El ID proporcionado de la Persona no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con Empresas
        List<EmpresaPersona> relacionesEmpresa = empresaPersonaRepository.findByPersona(persona.get());
        if (!relacionesEmpresa.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "La persona tiene una relación con Empresa/s (Empresa-Persona) y no puede ser eliminada.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        // Verificar relaciones con Inmuebles
        List<InmueblePersona> relacionesInmueble = inmueblePersonaRepository.findByPersona(persona.get());
        if (!relacionesInmueble.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "La persona tiene una relación con Inmueble/s (Inmueble-Persona) y no puede ser eliminada.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        // Eliminar persona
        personaRepository.deleteById(personaId);
        ErrorDTO successDTO = new ErrorDTO("200 OK", "Persona eliminada correctamente.");
        return ResponseEntity.ok(successDTO);
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



