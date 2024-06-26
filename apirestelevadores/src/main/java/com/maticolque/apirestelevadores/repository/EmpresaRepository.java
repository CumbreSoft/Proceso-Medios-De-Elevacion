package com.maticolque.apirestelevadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maticolque.apirestelevadores.model.Empresa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{

    //LISTAR CON PARAMETROS
    //List<Empresa> findByActiva(boolean activa);
}
