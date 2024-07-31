package com.maticolque.apirestelevadores.dto;


import com.maticolque.apirestelevadores.model.EmpresaPersona;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaPersonaReadDTO {

    private int epe_id;
    private boolean epe_es_dueno_emp;
    private boolean epe_es_reptec_emp;
    private EmpresaDTO empresa; // Detalles de la Empresa
    private PersonaDTO persona; // Detalles de la Persona

    public static EmpresaPersonaReadDTO fromEntity(EmpresaPersona empresaPersona) {

        EmpresaPersonaReadDTO dto = new EmpresaPersonaReadDTO();
        dto.setEpe_id(empresaPersona.getEpe_id());
        dto.setEpe_es_dueno_emp(empresaPersona.isEpe_es_dueno_emp());
        dto.setEpe_es_reptec_emp(empresaPersona.isEpe_es_reptec_emp());
        dto.setEmpresa(EmpresaDTO.fromEntity(empresaPersona.getEmpresa()));
        dto.setPersona(PersonaDTO.fromEntity(empresaPersona.getPersona()));
        return dto;
    }
}
