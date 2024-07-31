package com.maticolque.apirestelevadores.dtosimple;


import com.maticolque.apirestelevadores.model.Persona;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonaSimpleDTO {
    private int per_id;
    private String per_apellido;

    public static PersonaSimpleDTO fromEntity(Persona persona) {
        PersonaSimpleDTO dto = new PersonaSimpleDTO();
        dto.setPer_id(persona.getPer_id());
        dto.setPer_apellido(persona.getPer_apellido());
        return dto;
    }
}
