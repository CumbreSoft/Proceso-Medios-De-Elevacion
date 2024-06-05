package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.MedioHabilitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioHabilitacionRepository extends JpaRepository<MedioHabilitacion, Integer>{
}
