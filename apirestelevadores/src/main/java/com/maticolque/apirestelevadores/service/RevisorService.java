package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.repository.RevisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RevisorService {

    @Autowired
    private RevisorRepository revisorRepository;

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

    //ELiminar Revisor
    public void deleteRevisorById(Integer id){
        revisorRepository.deleteById(id);
    }


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
