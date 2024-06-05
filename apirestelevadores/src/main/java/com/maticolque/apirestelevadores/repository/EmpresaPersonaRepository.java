package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.EmpresaPersona;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaPersonaRepository extends JpaRepository<EmpresaPersona, Integer>{
}
