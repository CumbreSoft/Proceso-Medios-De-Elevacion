package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{
}
