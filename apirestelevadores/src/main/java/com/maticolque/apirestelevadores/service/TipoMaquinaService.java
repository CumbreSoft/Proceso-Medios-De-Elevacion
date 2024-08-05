package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.TipoMaquina;
import com.maticolque.apirestelevadores.repository.TipoMaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoMaquinaService {

    @Autowired
    private TipoMaquinaRepository tipoMaquinaRepository;

    //LISTAR
    public List<TipoMaquina> getAllTipoMaquina(){
        return tipoMaquinaRepository.findAll();
    }

    //BUSCAR POR ID
    public TipoMaquina buscartipoMaquinaPorId(Integer id)
    {
        return tipoMaquinaRepository.findById(id).orElse(null);
    }

    //CREAR
    public TipoMaquina createTipoMaquina(TipoMaquina tipoMaquina){
        return tipoMaquinaRepository.save(tipoMaquina);
    }

    //EDITAR
    public TipoMaquina updateTipoMaquina(TipoMaquina tipoMaquina){
        return tipoMaquinaRepository.save(tipoMaquina);
    }

    //ELIMINAR
    public void deleteTipoMaquinaById(Integer id){
        tipoMaquinaRepository.deleteById(id);
    }
}
