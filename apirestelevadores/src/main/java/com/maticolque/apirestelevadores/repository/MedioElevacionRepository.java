package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.MedioElevacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioElevacionRepository extends JpaRepository<MedioElevacion, Integer>{
}
