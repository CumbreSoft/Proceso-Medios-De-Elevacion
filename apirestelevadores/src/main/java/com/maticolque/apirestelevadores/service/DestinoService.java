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

    //LISTAR
    public List<Destino> getAllDestino(){
        return destinoRepository.findAll();
    }

    //BUSCAR POR ID
    public Destino buscarDestinoPorId(Integer id)
    {
        return destinoRepository.findById(id).orElse(null);
    }

    //CREAR
    public Destino createDestino(Destino destino){
        return destinoRepository.save(destino);
    }

    //EDITAR
    public Destino updateDestino(Destino destino){
        return destinoRepository.save(destino);
    }

    //ELIMINAR
    public void deleteDestinoById(Integer id){
        destinoRepository.deleteById(id);
    }
}
