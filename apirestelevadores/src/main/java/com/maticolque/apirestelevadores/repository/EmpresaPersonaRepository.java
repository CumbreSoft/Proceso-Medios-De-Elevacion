package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaPersonaRepository extends JpaRepository<EmpresaPersona, Integer>{


    //Obtener todas las relaciones para una empresa
    List<EmpresaPersona> findByEmpresa(Empresa empresa);

    List<EmpresaPersona> findByPersona(Persona persona);
}
