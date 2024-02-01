package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    //Mostrar Empresa
    public List<Empresa> getAllEmpresa(){
        return empresaRepository.findAll();
    }

    //Mostrar por ID
    public Empresa buscarEmpresaPorId(Integer id)
    {
        return empresaRepository.findById(id).orElse(null);
    }

    //Crear Empresa
    public Empresa createEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    //Editar Empresa
    public Empresa updateEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    //ELiminar Empresa
    public void deleteEmpresaById(Integer id){
        empresaRepository.deleteById(id);
    }
}
