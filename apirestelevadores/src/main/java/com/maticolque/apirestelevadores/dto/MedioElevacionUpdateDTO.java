package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioElevacionUpdateDTO {

    private int mde_tma_id; //ID Tipo Maquina
    private String mde_ubicacion;
    private String mde_tipo;
    private int mde_niveles;
    private boolean mde_planos_aprob;
    private String mde_expte_planos;
    private int mde_emp_id; //ID Empresa
    private boolean mde_activo;
}
