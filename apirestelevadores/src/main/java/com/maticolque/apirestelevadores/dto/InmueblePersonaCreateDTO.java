package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InmueblePersonaCreateDTO {

    private int ipe_id;
    private boolean ipe_es_admin_edif;
    private boolean ipe_es_coprop_edif;
    private int ipe_inm_id; //ID INMUEBLE
    private int ipe_per_id; //ID PERSONA

    public static InmueblePersona toEntity(InmueblePersonaCreateDTO dto, Inmueble inmueble, Persona persona) {

        InmueblePersona inmueblePersona = new InmueblePersona();
        inmueblePersona.setIpe_id(dto.getIpe_id());
        inmueblePersona.setIpe_es_admin_edif(dto.isIpe_es_admin_edif());
        inmueblePersona.setIpe_es_coprop_edif(dto.isIpe_es_coprop_edif());
        inmueblePersona.setInmueble(inmueble);
        inmueblePersona.setPersona(persona);
        return inmueblePersona;
    }
}
