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

    //Mostrar Tipo Adjunto
    public List<TipoAdjunto> getAllTipoAdjunto(){
        return tipoAdjuntoRepository.findAll();
    }

    //Mostrar por ID
    public TipoAdjunto buscarTipoAdjuntoPorId(Integer id)
    {
        return tipoAdjuntoRepository.findById(id).orElse(null);
    }

    //Crear Tipo Adjunto
    public TipoAdjunto createTipoAdjunto(TipoAdjunto tipoAdjunto){
        return tipoAdjuntoRepository.save(tipoAdjunto);
    }

    //Editar Tipo Adjunto
    public TipoAdjunto updateTipoAdjunto(TipoAdjunto tipoAdjunto){
        return tipoAdjuntoRepository.save(tipoAdjunto);
    }

    //ELiminar Tipo Adjunto
    public void deleteTipoAdjuntoById(Integer id){
        tipoAdjuntoRepository.deleteById(id);
    }
}
