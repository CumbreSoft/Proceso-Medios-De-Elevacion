package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.InmueblePersona;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InmueblePersonaReadDTO {

    private int ipe_id;
    private boolean ipe_es_admin_edif;
    private boolean ipe_es_coprop_edif;
    private InmuebleDTO inmueble; // Detalles de Inmueble
    private PersonaDTO persona; // Detalles de Persona

    public static InmueblePersonaReadDTO fromEntity(InmueblePersona inmueblePersona) {

        InmueblePersonaReadDTO dto = new InmueblePersonaReadDTO();
        dto.setIpe_id(inmueblePersona.getIpe_id());
        dto.setIpe_es_admin_edif(inmueblePersona.isIpe_es_admin_edif());
        dto.setIpe_es_coprop_edif(inmueblePersona.isIpe_es_coprop_edif());
        dto.setInmueble(InmuebleDTO.fromEntity(inmueblePersona.getInmueble()));
        dto.setPersona(PersonaDTO.fromEntity(inmueblePersona.getPersona()));
        return dto;
    }
}
