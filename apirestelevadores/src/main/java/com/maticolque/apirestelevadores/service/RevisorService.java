package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Revisor;
import com.maticolque.apirestelevadores.repository.RevisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
