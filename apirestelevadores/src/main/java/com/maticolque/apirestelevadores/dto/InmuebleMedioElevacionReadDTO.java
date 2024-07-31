package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.InmuebleMedioElevacion;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleMedioElevacionReadDTO {

    private int ime_id;
    private InmuebleDTO inmueble; // Detalles del Inmueble
    private MedioElevacionDTO medioElevacion; // Detalles del Medio de Elevacion

    public static InmuebleMedioElevacionReadDTO fromEntity(InmuebleMedioElevacion inmuebleMedioElevacion) {

        InmuebleMedioElevacionReadDTO dto = new InmuebleMedioElevacionReadDTO();
        dto.setIme_id(inmuebleMedioElevacion.getIme_id());
        dto.setInmueble(InmuebleDTO.fromEntity(inmuebleMedioElevacion.getInmueble()));
        dto.setMedioElevacion(MedioElevacionDTO.fromEntity(inmuebleMedioElevacion.getMedioElevacion()));
        return dto;
    }
}
