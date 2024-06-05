package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import org.springframework.stereotype.Repository;

@Repository
public interface HabilitacionDocumentoRepository extends JpaRepository<HabilitacionDocumento, Integer>{
}
