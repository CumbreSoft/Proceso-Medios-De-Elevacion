package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.MedioHabilitacion;
import com.maticolque.apirestelevadores.model.Revisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedioHabilitacionRepository extends JpaRepository<MedioHabilitacion, Integer>{

    List<MedioHabilitacion> findByRevisor(Revisor revisor);
}
