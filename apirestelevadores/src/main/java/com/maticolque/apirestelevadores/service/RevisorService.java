package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import com.maticolque.apirestelevadores.model.MedioHabilitacion;
import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.repository.EmpresaHabilitacionRepository;
import com.maticolque.apirestelevadores.repository.MedioHabilitacionRepository;
import com.maticolque.apirestelevadores.repository.RevisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RevisorService {

    @Autowired
    private RevisorRepository revisorRepository;

    @Autowired
    private EmpresaHabilitacionRepository empresaHabilitacionRepository;

    private MedioHabilitacionRepository medioHabilitacionRepository;


    //Mostrar Revisor
    public List<Revisor> getAllRevisor(){
        return revisorRepository.findAll();
    }

    //Mostrar por ID
    public Revisor buscarRevisorPorId(Integer id)
    {
        return revisorRepository.findById(id).orElse(null);
    }

    //Crear Revisor
    public Revisor createRevisor(Revisor revisor){
        return revisorRepository.save(revisor);
    }

    //Editar Revisor
    public Revisor updateRevisor(Revisor revisor){
        return revisorRepository.save(revisor);
    }

    //Eliminar Revisor
    public void deleteRevisorById(Integer id){
        revisorRepository.deleteById(id);
    }



    // *************** LOGICA PARA ELIMINAR UNA REVISOR ***************
    public ResponseEntity<ErrorDTO> eliminarRevisorSiNoTieneRelaciones(int revisorId) {
        // Buscar Revisor
        Optional<Revisor> revisor = revisorRepository.findById(revisorId);
        if (!revisor.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El ID proporcionado del Revisor no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con EmpresaHabilitacion
        List<EmpresaHabilitacion> relacionesEmpresaHabilitacion = empresaHabilitacionRepository.findByRevisor(revisor.get());
        if (!relacionesEmpresaHabilitacion.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El Revisor tiene una relación con una Empresa (EmpresaHabilitacion) y no puede ser eliminado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones con MedioHabilitacion
        List<MedioHabilitacion> relacionesMedioHabilitacion = medioHabilitacionRepository.findByRevisor(revisor.get());
        if (!relacionesMedioHabilitacion.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El Revisor tiene una relación con un Medio de Elevacion (MedioHabilitacion) y no puede ser eliminado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Eliminar Revisor
        revisorRepository.deleteById(revisorId);
        ErrorDTO successDTO = new ErrorDTO("200 OK", "Revisor eliminado correctamente.");
        return ResponseEntity.ok(successDTO);
    }
    // *************** LOGICA PARA ELIMINAR UNA REVISOR ***************





    // Método para filtrar revisores basado en el parámetro
    public List<Revisor> getRevisoresByParameter(int valor) {
        List<Revisor> revisores = revisorRepository.findAll();
        switch (valor) {
            case 1:
                return revisores.stream()
                        .filter(revisor -> (revisor.isRev_aprob_mde() || revisor.isRev_renov_mde()) && !revisor.isRev_aprob_emp())
                        .collect(Collectors.toList());
            case 2:
                return revisores.stream()
                        .filter(revisor -> revisor.isRev_aprob_emp() && !revisor.isRev_aprob_mde() && !revisor.isRev_renov_mde())
                        .collect(Collectors.toList());
            case 0:
            default:
                return revisores;
        }
    }
}
