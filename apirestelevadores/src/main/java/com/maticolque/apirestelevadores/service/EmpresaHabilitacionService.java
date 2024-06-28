package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.repository.EmpresaHabilitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaHabilitacionService {

    @Autowired
    private EmpresaHabilitacionRepository empresaHabilitacionRepository;

    //Mostrar Empresa Habilitacion
    public List<EmpresaHabilitacion> getAllEmpresaHabilitacion(){
        return empresaHabilitacionRepository.findAll();
    }

    //Mostrar por ID
    public EmpresaHabilitacion buscarEmpresaHabilitacionPorId(Integer id)
    {
        return empresaHabilitacionRepository.findById(id).orElse(null);
    }

    //Crear Empresa Habilitacion
    public EmpresaHabilitacion createEmpresaHabilitacion(EmpresaHabilitacion empresaHabilitacion){
        return empresaHabilitacionRepository.save(empresaHabilitacion);
    }

    //Editar Empresa Habilitacion
    public EmpresaHabilitacion updateEmpresaHabilitacion(EmpresaHabilitacion empresaHabilitacion){
        return empresaHabilitacionRepository.save(empresaHabilitacion);
    }

    //Eliminar Empresa Habilitacion
    public void deleteEmpresaHabilitacionById(Integer id){
        empresaHabilitacionRepository.deleteById(id);
    }


    //Metodo para buscar si un Revisor tiene una relacion con una Empresa (EmpresaHabilitacion)
    public boolean verificarRelacionRevisorEnEH(Integer revisorId) {
        List<EmpresaHabilitacion> relaciones = empresaHabilitacionRepository.findAll();
        return relaciones.stream().anyMatch(eh -> eh.getRevisor().getRev_id() == revisorId);
    }
}
