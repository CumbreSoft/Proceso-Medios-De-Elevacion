package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.InmueblePersona;

public interface InmueblePersonaRepository extends JpaRepository<InmueblePersona, Integer>{
}
