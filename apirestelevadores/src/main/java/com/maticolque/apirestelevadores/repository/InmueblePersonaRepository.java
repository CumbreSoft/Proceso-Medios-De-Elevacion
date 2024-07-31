package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InmueblePersonaRepository extends JpaRepository<InmueblePersona, Integer>{

    List<InmueblePersona> findByPersona(Persona persona);

    List<InmueblePersona> findByInmueble(Inmueble inmueble);
}
