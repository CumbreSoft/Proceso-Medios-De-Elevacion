package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.TipoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAdjuntoRepository extends JpaRepository<TipoAdjunto, Integer>{
}
