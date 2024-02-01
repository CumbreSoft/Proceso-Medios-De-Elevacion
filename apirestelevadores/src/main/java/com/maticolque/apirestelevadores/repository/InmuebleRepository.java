package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {

}

