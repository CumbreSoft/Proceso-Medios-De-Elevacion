package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.InmueblePersona;
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

    //LISTAR
    public List<EmpresaPersona> getAllEmpresaPersona(){
        return empresaPersonaRepository.findAll();
    }

    //BUSCAR POR ID
    public EmpresaPersona buscarEmpresaPersonaPorId(Integer id) {return empresaPersonaRepository.findById(id).orElse(null);}

    //CREAR
    public EmpresaPersona createEmpresaPersona(EmpresaPersona empresaPersona){return empresaPersonaRepository.save(empresaPersona);}

    //EDITAR
    public EmpresaPersona updateEmpresaPersona(EmpresaPersona empresaPersona){return empresaPersonaRepository.save(empresaPersona);}

    //ELIMINAR
    public void deleteEmpresaPersonaById(Integer id){
        empresaPersonaRepository.deleteById(id);
    }


    //METODO PARA VERIFICAR SI UNA EMPRESA TIENE RELACION CON PERSONAS
    public boolean verificarRelacionesConPersonas(Integer empresaId) {
        List<EmpresaPersona> relaciones = empresaPersonaRepository.findAll();
        return relaciones.stream().anyMatch(ep -> ep.getEmpresa().getEmp_id() == empresaId);
    }


    //METODO PARA OBTENER TODAS LAS PERSONAS RELACIONADAS CON UNA EMPRESA ESPECIFICA
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
