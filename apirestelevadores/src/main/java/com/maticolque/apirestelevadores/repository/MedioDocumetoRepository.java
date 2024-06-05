package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.MedioDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioDocumetoRepository extends JpaRepository<MedioDocumento, Integer>{
}
