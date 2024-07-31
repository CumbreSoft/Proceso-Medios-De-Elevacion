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

    //Listar todas las Empresas Habilitacion
    public List<EmpresaHabilitacion> getAllEmpresaHabilitacion(){
        return empresaHabilitacionRepository.findAll();
    }

    //Listar Empresa Habilitacion por ID
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


    // Método para verificar si una empresa ya tiene un revisor asignado
    public boolean empresaTieneRevisorAsignado(Integer empresaId) {
        List<EmpresaHabilitacion> habilitaciones = empresaHabilitacionRepository.findAll();
        return habilitaciones.stream().anyMatch(hab -> hab.getEmpresa().getEmp_id() == empresaId && hab.getRevisor() != null);
    }
}
