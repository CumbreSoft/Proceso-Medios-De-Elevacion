package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.TipoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMaquinaRepository extends JpaRepository<TipoMaquina, Integer>{
}
