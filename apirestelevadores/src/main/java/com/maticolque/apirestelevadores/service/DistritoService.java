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

    //LISTAR
    public List<Distrito> getAllDistrito(){
        return distritoRepository.findAll();
    }

    //BUSCAR POR ID
    public Distrito buscarDistritoPorId(Integer id)
    {
        return distritoRepository.findById(id).orElse(null);
    }

    //CREAR
    public Distrito createDistrito(Distrito distrito){
        return distritoRepository.save(distrito);
    }

    //EDITAR
    public Distrito updateDistrito(Distrito distrito){
        return distritoRepository.save(distrito);
    }

    //ELIMINAR
    public void deleteDistritoById(Integer id){
        distritoRepository.deleteById(id);
    }
}
