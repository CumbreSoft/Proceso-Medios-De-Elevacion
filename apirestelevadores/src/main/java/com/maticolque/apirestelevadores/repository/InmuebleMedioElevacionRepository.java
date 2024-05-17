package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleMedioElevacionRepository extends JpaRepository<InmuebleMedioElevacion, Integer> {

}

