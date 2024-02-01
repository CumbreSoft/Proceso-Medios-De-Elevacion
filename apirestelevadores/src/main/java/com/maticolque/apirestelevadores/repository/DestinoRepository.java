package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.Destino;

public interface DestinoRepository extends JpaRepository<Destino, Integer> {
}
