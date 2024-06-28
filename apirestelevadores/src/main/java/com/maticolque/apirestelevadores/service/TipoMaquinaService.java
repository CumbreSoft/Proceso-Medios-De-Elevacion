package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.TipoMaquina;
import com.maticolque.apirestelevadores.repository.TipoMaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoMaquinaService {

    @Autowired
    private TipoMaquinaRepository tipoMaquinaRepository;

    //Mostrar Tipo Maquina
    public List<TipoMaquina> getAllTipoMaquina(){
        return tipoMaquinaRepository.findAll();
    }

    //Mostrar por ID
    public TipoMaquina buscartipoMaquinaPorId(Integer id)
    {
        return tipoMaquinaRepository.findById(id).orElse(null);
    }

    //Crear Tipo Maquina
    public TipoMaquina createTipoMaquina(TipoMaquina tipoMaquina){
        return tipoMaquinaRepository.save(tipoMaquina);
    }

    //Editar Tipo Maquina
    public TipoMaquina updateTipoMaquina(TipoMaquina tipoMaquina){
        return tipoMaquinaRepository.save(tipoMaquina);
    }

    //Eliminar Tipo Maquina
    public void deleteTipoMaquinaById(Integer id){
        tipoMaquinaRepository.deleteById(id);
    }

}
