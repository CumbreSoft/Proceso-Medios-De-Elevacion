package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.model.*;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import com.maticolque.apirestelevadores.repository.InmueblePersonaRepository;
import com.maticolque.apirestelevadores.repository.InmuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private InmueblePersonaRepository inmueblePersonaRepository;

    @Autowired
    private InmuebleMedioElevacionRepository inmuebleMedioElevacionRepository;




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
    public ResponseEntity<ErrorDTO> eliminarInmuebleSiNoTieneRelaciones(int inmuebleId) {
        // Buscar inmueble
        Optional<Inmueble> inmueble = inmuebleRepository.findById(inmuebleId);
        if (!inmueble.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El ID proporcionado del Inmueble no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con Personas
        List<InmueblePersona> relacionesPersona = inmueblePersonaRepository.findByInmueble(inmueble.get());
        if (!relacionesPersona.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El inmueble tiene una relación con Persona/s (Inmueble-Persona) y no puede ser eliminado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con Medio Elevación
        List<InmuebleMedioElevacion> relacionesMedioElevacion = inmuebleMedioElevacionRepository.findByInmueble(inmueble.get());
        if (!relacionesMedioElevacion.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El inmueble tiene una relación con Medio Elevación (Inmueble-MDE) y no puede ser eliminado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Eliminar inmueble
        inmuebleRepository.deleteById(inmuebleId);
        ErrorDTO successDTO = new ErrorDTO("200 OK", "Inmueble eliminado correctamente.");
        return ResponseEntity.ok(successDTO);
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
