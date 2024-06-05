package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaHabilitacionRepository extends JpaRepository<EmpresaHabilitacion, Integer>{
}
