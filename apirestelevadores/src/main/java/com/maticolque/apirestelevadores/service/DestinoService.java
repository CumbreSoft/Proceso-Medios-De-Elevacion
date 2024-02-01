package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.repository.DestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinoService {

    @Autowired
    private DestinoRepository destinoRepository;

    //Mostrar Destino
    public List<Destino> getAllDestino(){
        return destinoRepository.findAll();
    }

    //Mostrar por ID
    public Destino buscarDestinoPorId(Integer id)
    {
        return destinoRepository.findById(id).orElse(null);
    }

    //Crear Destino
    public Destino createDestino(Destino destino){
        return destinoRepository.save(destino);
    }

    //Editar Destino
    public Destino updateDestino(Destino destino){
        return destinoRepository.save(destino);
    }

    //ELiminar Destino
    public void deleteDestinoById(Integer id){
        destinoRepository.deleteById(id);
    }
}
