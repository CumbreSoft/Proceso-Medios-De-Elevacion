package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import com.maticolque.apirestelevadores.model.TipoMaquina;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class InmuebleMedioElevacionService {

    @Autowired
    private InmuebleMedioElevacionRepository inmuebleMedioElevacionRepository;

    //LISTAR
    public List<InmuebleMedioElevacion> getAllInmueblesMDE(){
        return inmuebleMedioElevacionRepository.findAll();
    }

    //BUSCAR POR ID
    public InmuebleMedioElevacion buscarInmuebleMDEPorId(Integer id) {return inmuebleMedioElevacionRepository.findById(id).orElse(null);}

    //CREAR
    public InmuebleMedioElevacion createInmuebleMDE(InmuebleMedioElevacion inmuebleMedioElevacion){return inmuebleMedioElevacionRepository.save(inmuebleMedioElevacion);}

    //EDITAR
    public InmuebleMedioElevacion updateInmuebleMDE(InmuebleMedioElevacion inmuebleMedioElevacion){return inmuebleMedioElevacionRepository.save(inmuebleMedioElevacion);}

    //ELIMINAR
    public void deleteInmuebleMDEById(Integer id){
        inmuebleMedioElevacionRepository.deleteById(id);
    }


    //METODO PARA FILTRAR LOS MEDIOS DE ELEVACION POR NUMERO DE PADRON (INMUEBLE)
    public List<InmuebleMedioElevacion> obtenerMediosDeElevacionPorInmuebleId(Integer inmuebleId) {
        List<InmuebleMedioElevacion> inmuebleMedioElevacionList = inmuebleMedioElevacionRepository.findAll();
        List<InmuebleMedioElevacion> mediosDeElevacionPorInmueble = new ArrayList<>();

        for (InmuebleMedioElevacion ime : inmuebleMedioElevacionList) {
            if (ime.getInmueble().getInm_id() == inmuebleId) {
                mediosDeElevacionPorInmueble.add(ime);
            }
        }

        return mediosDeElevacionPorInmueble;
    }

    //OBTENER LA PRIMERA RELACION DE CADA INMUEBLE
    public List<InmuebleMedioElevacion> obtenerPrimerInmuebleMedioElevacion() {
        // Lógica para obtener el primer InmuebleMedioElevacion relacionado para cada Inmueble
        // Esto podría involucrar una consulta SQL con una subconsulta o un DISTINCT ON.
        return inmuebleMedioElevacionRepository.obtenerPrimerInmuebleMedioElevacion();
    }
}
