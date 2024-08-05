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

    //LISTAR
    public List<EmpresaHabilitacion> getAllEmpresaHabilitacion(){
        return empresaHabilitacionRepository.findAll();
    }

    //BUSCAR POR ID
    public EmpresaHabilitacion buscarEmpresaHabilitacionPorId(Integer id) {return empresaHabilitacionRepository.findById(id).orElse(null);}

    //CREAR
    public EmpresaHabilitacion createEmpresaHabilitacion(EmpresaHabilitacion empresaHabilitacion){return empresaHabilitacionRepository.save(empresaHabilitacion);}

    //EDITAR
    public EmpresaHabilitacion updateEmpresaHabilitacion(EmpresaHabilitacion empresaHabilitacion){return empresaHabilitacionRepository.save(empresaHabilitacion);}

    //ELIMINAR
    public void deleteEmpresaHabilitacionById(Integer id){
        empresaHabilitacionRepository.deleteById(id);
    }


    //METODO PARA VERIFICAR SI UNA EMPRESA YA TIENE UN REVISOR ASIGNADO
    public boolean empresaTieneRevisorAsignado(Integer empresaId) {
        List<EmpresaHabilitacion> habilitaciones = empresaHabilitacionRepository.findAll();
        return habilitaciones.stream().anyMatch(hab -> hab.getEmpresa().getEmp_id() == empresaId && hab.getRevisor() != null);
    }
}