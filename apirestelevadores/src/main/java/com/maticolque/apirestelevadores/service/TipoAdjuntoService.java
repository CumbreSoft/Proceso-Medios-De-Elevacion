package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.TipoAdjunto;
import com.maticolque.apirestelevadores.repository.TipoAdjuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoAdjuntoService {

    @Autowired
    private TipoAdjuntoRepository tipoAdjuntoRepository;


    //CREAR
    public List<TipoAdjunto> getAllTipoAdjunto(){
        return tipoAdjuntoRepository.findAll();
    }

    //BUSCAR POR ID
    public TipoAdjunto buscarTipoAdjuntoPorId(Integer id)
    {
        return tipoAdjuntoRepository.findById(id).orElse(null);
    }

    //CREAR
    public TipoAdjunto createTipoAdjunto(TipoAdjunto tipoAdjunto){
        return tipoAdjuntoRepository.save(tipoAdjunto);
    }

    //EDITAR
    public TipoAdjunto updateTipoAdjunto(TipoAdjunto tipoAdjunto){
        return tipoAdjuntoRepository.save(tipoAdjunto);
    }

    //ELIMINAR
    public void deleteTipoAdjuntoById(Integer id){
        tipoAdjuntoRepository.deleteById(id);
    }
}
