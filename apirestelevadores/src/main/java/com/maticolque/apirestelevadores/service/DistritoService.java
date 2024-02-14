package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.repository.DistritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistritoService {
    @Autowired
    private DistritoRepository distritoRepository;

    //Mostrar
    public List<Distrito> getAllDistrito(){
        return distritoRepository.findAll();
    }

    //Mostrar por ID
    public Distrito buscarDistritoPorId(Integer id)
    {
        return distritoRepository.findById(id).orElse(null);
    }

    //Crear
    public Distrito createDistrito(Distrito distrito){
        return distritoRepository.save(distrito);
    }

    //Editar
    public Distrito updateDistrito(Distrito distrito){
        return distritoRepository.save(distrito);
    }

    //ELiminar
    public void deleteDistritoById(Integer id){
        distritoRepository.deleteById(id);
    }
}
