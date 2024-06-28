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

    @Autowired
    private EmpresaPersonaService empresaPersonaService;

    @Autowired
    private MedioElevacionService medioElevacionService;

    @Autowired
    private EmpresaHabilitacionService empresaHabilitacionService;

    @Autowired
    private MedioHabilitacionService medioHabilitacionService;

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

    //Eliminar Empresa
    public void deleteEmpresaById(Integer id){
        empresaRepository.deleteById(id);
    }


    /* ******************************************************************************
    Este codigo llama a los metodos de las clases que tienen una relación con empresa,
    verifica que no exista una relación antes de eliminar de eliminar una empresa. */
    public boolean verificarRelacionEmpresaEnEP(Integer empresaId) {
        return empresaPersonaService.verificarRelacionEmpresaEnEP(empresaId);
    }

    public String eliminarEmpresaSiNoTieneRelaciones(Integer empresaId) {
        Empresa empresaExistente = buscarEmpresaPorId(empresaId);
        if (empresaExistente == null) {
            return "El ID proporcionado de la Empresa no existe.";
        }

        if (verificarRelacionEmpresaEnEP(empresaId)) {
            return "La empresa tiene una relación con Persona/s (Empresas-Persona) y no puede ser eliminada.";
        }

        deleteEmpresaById(empresaId);
        return "Empresa eliminada correctamente.";
    }
    /* ****************************************************************************** */




    // Método para obtener una empresa por su ID
    public Empresa obtenerEmpresaPorId(Integer empresaId) {

        // Obtener todas las empresas del repositorio
        List<Empresa> empresas = empresaRepository.findAll();

        // Buscar la empresa con el ID proporcionado
        for (Empresa empresa : empresas) {
            if (empresa.getEmp_id() == empresaId) {
                // Devolver la empresa si se encuentra
                return empresa;
            }
        }

        // Devolver null si no se encuentra ninguna empresa con el ID proporcionado
        return null;
    }

    //LISTAR CON PARAMETROS
    /*public List<Empresa> getEmpresasByActiva(Boolean activa) {
        return empresaRepository.findByActiva(activa);
    }*/


}
