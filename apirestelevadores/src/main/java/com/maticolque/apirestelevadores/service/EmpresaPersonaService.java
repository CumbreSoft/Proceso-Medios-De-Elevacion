package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpresaPersonaService {

    @Autowired
    private EmpresaPersonaRepository empresaPersonaRepository;

    //Mostrar Empresa Persona
    public List<EmpresaPersona> getAllEmpresaPersona(){
        return empresaPersonaRepository.findAll();
    }

    //Mostrar por ID
    public EmpresaPersona buscarEmpresaPersonaPorId(Integer id)
    {
        return empresaPersonaRepository.findById(id).orElse(null);
    }

    //Crear Empresa Persona
    public EmpresaPersona createEmpresaPersona(EmpresaPersona empresaPersona){
        return empresaPersonaRepository.save(empresaPersona);
    }

    //Editar Empresa Persona
    public EmpresaPersona updateEmpresaPersona(EmpresaPersona empresaPersona){
        return empresaPersonaRepository.save(empresaPersona);
    }

    //ELiminar Empresa Persona
    public void deleteEmpresaPersonaById(Integer id){
        empresaPersonaRepository.deleteById(id);
    }

    // Método para obtener todas las personas relacionadas con una empresa específica
    public List<Persona> obtenerPersonasPorEmpresa(Integer empresaId) {
        List<EmpresaPersona> empresaPersonaList = empresaPersonaRepository.findAll();
        List<Persona> personasRelacionadas = new ArrayList<>();

        for (EmpresaPersona empresaPersona : empresaPersonaList) {
            if (empresaPersona.getEmpresa().getEmp_id() == empresaId) {
                personasRelacionadas.add(empresaPersona.getPersona());
            }
        }

        return personasRelacionadas;
    }
}
