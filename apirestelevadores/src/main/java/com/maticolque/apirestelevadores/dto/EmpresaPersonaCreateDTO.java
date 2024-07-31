package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmpresaPersonaCreateDTO {

    private int epe_id;
    private boolean epe_es_dueno_emp;
    private boolean epe_es_reptec_emp;
    private int epe_emp_id; // ID de Empresa
    private int epe_per_id; // ID de Persona

    public static EmpresaPersona toEntity(EmpresaPersonaCreateDTO dto, Empresa empresa, Persona persona) {

        EmpresaPersona empresaPersona = new EmpresaPersona();
        empresaPersona.setEpe_id(dto.getEpe_id());
        empresaPersona.setEpe_es_dueno_emp(dto.isEpe_es_dueno_emp());
        empresaPersona.setEpe_es_reptec_emp(dto.isEpe_es_reptec_emp());
        empresaPersona.setEmpresa(empresa);
        empresaPersona.setPersona(persona);
        return empresaPersona;
    }
}
