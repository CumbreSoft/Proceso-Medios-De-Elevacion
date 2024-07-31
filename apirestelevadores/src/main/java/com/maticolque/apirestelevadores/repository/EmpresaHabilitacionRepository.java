package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Revisor;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaHabilitacionRepository extends JpaRepository<EmpresaHabilitacion, Integer>{

    //Buscar revisor
    List<EmpresaHabilitacion> findByRevisor(Revisor revisor);
}
