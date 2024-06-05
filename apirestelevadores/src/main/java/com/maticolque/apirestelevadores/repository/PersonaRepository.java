package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer>{
}
