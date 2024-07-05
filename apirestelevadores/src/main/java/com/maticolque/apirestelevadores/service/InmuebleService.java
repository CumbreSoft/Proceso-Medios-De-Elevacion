package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.InmuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private InmueblePersonaService inmueblePersonaService;

    @Autowired
    private InmuebleMedioElevacionService inmuebleMedioElevacionService;

    //Mostrar inmueble
    public List<Inmueble> getAllInmuebles(){
        return inmuebleRepository.findAll();
    }

    //Mostrar por ID
    public Inmueble buscarInmueblePorId(Integer id)
    {
        return inmuebleRepository.findById(id).orElse(null);
    }

    // Mostrar por ID
    public Inmueble buscarInmueblePorIds(Integer id) {
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

    //Eliminar inmueble
    public void deleteInmuebleById(Integer id){
        inmuebleRepository.deleteById(id);
    }


    // *************** LOGICA PARA ELIMINAR UN INMUEBLE ***************

    //Verificar si hay relacion de un Inmueble en InmueblePersona
    public boolean verificarRelacionInmuebleEnIP(Integer inmuebleId) {
        return inmueblePersonaService.verificarRelacionInmuebleEnIP(inmuebleId);
    }

    // Verificar si hay relacion de un Inmueble en MedioElevacion
    public boolean verificarRelacionInmuebleEnME(Integer inmuebleId) {
        return inmuebleMedioElevacionService.verificarRelacionInmuebleEnIMDE(inmuebleId);
    }

    public String eliminarInmuebleSiNoTieneRelaciones(Integer inmuebleId) {
        Inmueble inmuebleExistente = buscarInmueblePorId(inmuebleId);
        if (inmuebleExistente == null) {
            return "El ID proporcionado del Inmueble no existe.";
        }

        if (verificarRelacionInmuebleEnIP(inmuebleId)) {
            return "El Inmueble tiene una relación con una Persona (Inmueble-Persona) y no puede ser eliminado.";
        }

        if (verificarRelacionInmuebleEnME(inmuebleId)) {
            return "El Inmueble tiene una relación con un Medio de Elevación y no puede ser eliminado.";
        }

        deleteInmuebleById(inmuebleId);
        return "Inmueble eliminado correctamente.";
    }
    // *************** LOGICA PARA ELIMINAR UNA INMUEBLE ***************




    // Buscar inmueble por padron
    public Inmueble buscarInmueblePorPadron(Integer inmPadron) {
        List<Inmueble> inmuebles = inmuebleRepository.findAll();
        for (Inmueble inmueble : inmuebles) {
            if (inmueble.getInm_padron() == inmPadron) {
                return inmueble;
            }
        }
        return null; // Devuelve null si no se encuentra el inmueble con el padron dado
    }
}
