package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import com.maticolque.apirestelevadores.repository.MedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedioElevacionService {

    @Autowired
    private MedioElevacionRepository medioElevacionRepository;

    @Autowired
    private InmuebleMedioElevacionRepository inmuebleMedioElevacionRepository;



    //Mostrar Medio Elevacion
    public List<MedioElevacion> getAllMedioElevacion(){
        return medioElevacionRepository.findAll();
    }

    //Mostrar por ID
    public MedioElevacion buscarMedioElevacionPorId(Integer id)
    {
        return medioElevacionRepository.findById(id).orElse(null);
    }

    //Crear Medio Elevacion
    public MedioElevacion createMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //Editar Medio Elevacion
    public MedioElevacion updateMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //Eliminar Medio Elevacion
    public void deleteMedioElevacionById(Integer id){
        medioElevacionRepository.deleteById(id);
    }


    // *************** LOGICA PARA ELIMINAR UN MDE ***************
    public ResponseEntity<ErrorDTO> eliminarMedioElevacionSiNoTieneRelaciones(int medioElevacionId) {
        // Buscar MedioElevacion
        Optional<MedioElevacion> medioElevacion = medioElevacionRepository.findById(medioElevacionId);
        if (!medioElevacion.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El ID proporcionado del Medio de Elevación no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con Inmuebles
        List<InmuebleMedioElevacion> relacionesInmueble = inmuebleMedioElevacionRepository.findByMedioElevacion(medioElevacion.get());
        if (!relacionesInmueble.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El Medio de Elevación tiene una relación con un Inmueble (Inmueble-MDE) y no puede ser eliminado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Eliminar MedioElevacion
        medioElevacionRepository.deleteById(medioElevacionId);
        ErrorDTO successDTO = new ErrorDTO("200 OK", "Medio de Elevación eliminado correctamente.");
        return ResponseEntity.ok(successDTO);
    }
    // *************** LOGICA PARA ELIMINAR UNA MDE ***************

}
