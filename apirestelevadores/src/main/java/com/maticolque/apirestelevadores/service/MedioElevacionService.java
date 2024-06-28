package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.RespuestaDTO;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import com.maticolque.apirestelevadores.repository.InmuebleMedioElevacionRepository;
import com.maticolque.apirestelevadores.repository.MedioElevacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioElevacionService {

    @Autowired
    private MedioElevacionRepository medioElevacionRepository;

    @Autowired
    private InmuebleMedioElevacionRepository inmuebleMedioElevacionRepository;

    @Autowired
    private InmuebleMedioElevacionService inmuebleMedioElevacionService;


    //Mostrar Medio Elevacion
    public List<MedioElevacion> getAllMedioElevacion(){
        return medioElevacionRepository.findAll();
    }

    //Mostrar por ID
    public MedioElevacion buscarMedioElevacionPorId(Integer id)
    {
        return medioElevacionRepository.findById(id).orElse(null);
    }

    //Crear Medio Elevacion
    public MedioElevacion createMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //Editar Medio Elevacion
    public MedioElevacion updateMedioElevacion(MedioElevacion medioElevacion){
        return medioElevacionRepository.save(medioElevacion);
    }

    //Eliminar Medio Elevacion
    public void deleteMedioElevacionById(Integer id){
        medioElevacionRepository.deleteById(id);
    }


    // *************** LOGICA PARA ELIMINAR UN MDE ***************

    //Verificar si hay relacion de un MDE en InmuebleMDE
    public boolean verificarRelacionMDEEnIMDE(Integer mdeId) {
        return inmuebleMedioElevacionService.verificarRelacionMDEEnIMDE(mdeId);
    }
    public String eliminarMDESiNoTieneRelaciones(Integer mdeId) {
        MedioElevacion mdeExistente = buscarMedioElevacionPorId(mdeId);
        if (mdeExistente == null) {
            return "El ID proporcionado del Medio de Elevación no existe.";
        }

        if (verificarRelacionMDEEnIMDE(mdeId)) {
            return "El Medio de Elevación tiene una relación con un Inmueble (Inmueble-MDE) y no puede ser eliminado.";
        }

        deleteMedioElevacionById(mdeId);
        return "Medio de Elevación eliminado correctamente.";
    }
    // *************** LOGICA PARA ELIMINAR UNA MDE ***************




    public List<InmuebleMedioElevacion> obtenerTodosLosInmueblesMedioElevacion() {
        return inmuebleMedioElevacionRepository.findAll();
    }
}
