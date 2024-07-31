package com.maticolque.apirestelevadores.service;

import com.maticolque.apirestelevadores.dto.ErrorDTO;
import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.repository.EmpresaPersonaRepository;
import com.maticolque.apirestelevadores.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaPersonaRepository empresaPersonaRepository;


    //Listar todas las Empresas
    public List<Empresa> getAllEmpresa(){
        return empresaRepository.findAll();
    }

    //Listar Empresa por ID
    public Empresa buscarEmpresaPorId(Integer id)
    {
        return empresaRepository.findById(id).orElse(null);
    }

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
    public ResponseEntity<ErrorDTO> eliminarEmpresaSiNoTieneRelaciones(int empresaId) {
        // Buscar empresa
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);
        if (!empresa.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("404 NOT FOUND", "El ID proporcionado de la Empresa no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }

        // Verificar relaciones
        List<EmpresaPersona> relaciones = empresaPersonaRepository.findByEmpresa(empresa.get());
        if (!relaciones.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO("400 BAD REQUEST", "La empresa tiene una relación con Persona/s (Empresa-Persona) y no puede ser eliminada.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        // Eliminar empresa
        empresaRepository.deleteById(empresaId);
        ErrorDTO successDTO = new ErrorDTO("200 OK", "Empresa eliminada correctamente.");
        return ResponseEntity.ok(successDTO);
    }
    /* ****************************************************************************** */



}
