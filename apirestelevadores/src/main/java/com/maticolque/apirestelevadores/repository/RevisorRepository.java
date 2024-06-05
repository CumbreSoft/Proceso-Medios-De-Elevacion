package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Revisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisorRepository extends JpaRepository<Revisor, Integer>{
}
