package com.maticolque.apirestelevadores.dtosimple;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.Persona;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmpresaPersonaSimpleDTO {

    private int emp_id;
    private String emp_razon;
    private List<PersonaSimpleDTO> personas;

    public static EmpresaPersonaSimpleDTO fromEntity(Empresa empresa, List<Persona> personas) {
        EmpresaPersonaSimpleDTO dto = new EmpresaPersonaSimpleDTO();
        dto.setEmp_id(empresa.getEmp_id());
        dto.setEmp_razon(empresa.getEmp_razon());

        List<PersonaSimpleDTO> personaDTOs = new ArrayList<>();
        for (Persona persona : personas) {
            personaDTOs.add(PersonaSimpleDTO.fromEntity(persona));
        }
        dto.setPersonas(personaDTOs);
        return dto;
    }
}
