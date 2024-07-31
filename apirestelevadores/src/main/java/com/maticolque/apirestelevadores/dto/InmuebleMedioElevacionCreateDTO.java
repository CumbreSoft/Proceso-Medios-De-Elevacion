package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InmuebleMedioElevacionCreateDTO {

    private int ime_id;
    private int ime_inm_id; // ID de Inmueble
    private int ime_mde_id; // ID de MDE

    public static InmuebleMedioElevacion toEntity(InmuebleMedioElevacionCreateDTO dto, Inmueble inmueble, MedioElevacion medioElevacion) {

        InmuebleMedioElevacion inmuebleMedioElevacion = new InmuebleMedioElevacion();
        inmuebleMedioElevacion.setIme_id(dto.getIme_id());
        inmuebleMedioElevacion.setInmueble(inmueble);
        inmuebleMedioElevacion.setMedioElevacion(medioElevacion);
        return inmuebleMedioElevacion;
    }
}
