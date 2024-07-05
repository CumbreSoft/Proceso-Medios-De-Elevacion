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

    //Listar todas las Empresas Personas
    public List<EmpresaPersona> getAllEmpresaPersona(){
        return empresaPersonaRepository.findAll();
    }

    //Listar Empresa Persona por ID
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

    public boolean verificarRelacionesConPersonas(Integer empresaId) {
        List<EmpresaPersona> relaciones = empresaPersonaRepository.findAll();
        return relaciones.stream().anyMatch(ep -> ep.getEmpresa().getEmp_id() == empresaId);
    }

    //Eliminar Empresa Persona
    public void deleteEmpresaPersonaById(Integer id){
        empresaPersonaRepository.deleteById(id);
    }



    /*  Metodo para buscar si EmpresaPersona tiene una relacion con empresa
        1)--> Llamo al método findAll() del repositorio empresaPersonaRepository.
        Este método devuelve una lista de todas las entidades EmpresaPersona que están
        almacenadas en la base de datos.
        2)-->relaciones.stream(): Convierte la lista relaciones en un flujo para poder realizar operaciones sobre ella.
        .anyMatch(ep -> ep.getEmpresa().getEmp_id() == empresaId): Itera sobre cada objeto EmpresaPersona (ep) en
        el flujo y verifica si el emp_id de la empresa asociada (ep.getEmpresa().getEmp_id()) es igual al empresaId
        que se pasa al método. */

    //Metodo para buscar si una Empresa tiene una relacion con una Persona (EmpresaPersona)
    public boolean verificarRelacionEmpresaEnEP(Integer empresaId) {
        List<EmpresaPersona> relaciones = empresaPersonaRepository.findAll();
        return relaciones.stream().anyMatch(ep -> ep.getEmpresa().getEmp_id() == empresaId);
    }

    //Metodo para buscar si una Persona tiene una relacion con una Empresa (EmpresaPersona)
    public boolean verificarRelacionPersonaEnEP(Integer personaId) {
        List<EmpresaPersona> relaciones = empresaPersonaRepository.findAll();
        return relaciones.stream().anyMatch(p -> p.getPersona().getPer_id() == personaId);
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
