package com.maticolque.apirestelevadores.repository;

import com.maticolque.apirestelevadores.model.Inmueble;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InmuebleMedioElevacionRepository extends JpaRepository<InmuebleMedioElevacion, Integer> {

    List<InmuebleMedioElevacion> findByInmueble(Inmueble inmueble);

    List<InmuebleMedioElevacion> findByMedioElevacion(MedioElevacion medioElevacion);


    //Listar primer Inmueble-MedioDeElevacion
    @Query("SELECT ime FROM InmuebleMedioElevacion ime WHERE ime.ime_id IN " +
            "(SELECT MIN(ime2.ime_id) FROM InmuebleMedioElevacion ime2 GROUP BY ime2.inmueble.inm_id)")
    List<InmuebleMedioElevacion> obtenerPrimerInmuebleMedioElevacion();


}

