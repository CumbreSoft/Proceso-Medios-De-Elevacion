package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaPersonaUpdateDTO {
    private boolean epe_es_dueno_emp;
    private boolean epe_es_reptec_emp;
    private int epe_emp_id; // ID de Empresa
}
