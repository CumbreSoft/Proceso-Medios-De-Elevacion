package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Persona;
import lombok.*;;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDTO {

    private int per_id;
    private String per_nombre;
    private String per_apellido;
    private String per_cuit;
    private int per_tipodoc;
    private String per_numdoc;
    private String per_telefono;
    private String per_correo;
    private String per_domic_legal;
    private boolean per_es_dueno_emp;
    private boolean per_es_reptec_emp;
    private boolean per_es_admin_edif;
    private boolean per_es_coprop_edif;
    private boolean per_activa;

    public static PersonaDTO fromEntity(Persona persona) {
        PersonaDTO dto = new PersonaDTO();
        dto.setPer_id(persona.getPer_id());
        dto.setPer_nombre(persona.getPer_nombre());
        dto.setPer_apellido(persona.getPer_apellido());
        dto.setPer_cuit(persona.getPer_cuit());
        dto.setPer_tipodoc(persona.getPer_tipodoc());
        dto.setPer_numdoc(persona.getPer_numdoc());
        dto.setPer_telefono(persona.getPer_telefono());
        dto.setPer_correo(persona.getPer_correo());
        dto.setPer_domic_legal(persona.getPer_domic_legal());
        dto.setPer_es_dueno_emp(persona.isPer_es_dueno_emp());
        dto.setPer_es_reptec_emp(persona.isPer_es_reptec_emp());
        dto.setPer_es_admin_edif(persona.isPer_es_admin_edif());
        dto.setPer_es_coprop_edif(persona.isPer_es_coprop_edif());
        dto.setPer_activa(persona.isPer_activa());

        return dto;
    }

    public static Persona toEntity(PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setPer_id(dto.getPer_id());
        persona.setPer_nombre(dto.getPer_nombre());
        persona.setPer_apellido(dto.getPer_apellido());
        persona.setPer_cuit(dto.getPer_cuit());
        persona.setPer_tipodoc(dto.getPer_tipodoc());
        persona.setPer_numdoc(dto.getPer_numdoc());
        persona.setPer_telefono(dto.getPer_telefono());
        persona.setPer_correo(dto.getPer_correo());
        persona.setPer_domic_legal(dto.getPer_domic_legal());
        persona.setPer_es_dueno_emp(dto.isPer_es_dueno_emp());
        persona.setPer_es_reptec_emp(dto.isPer_es_reptec_emp());
        persona.setPer_es_admin_edif(dto.isPer_es_admin_edif());
        persona.setPer_es_coprop_edif(dto.isPer_es_coprop_edif());
        persona.setPer_activa(dto.isPer_activa());

        return persona;
    }
}
