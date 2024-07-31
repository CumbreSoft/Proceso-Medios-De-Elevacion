package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaHabilitacionUpdateDTO {

    private String eha_fecha;
    private String eha_expediente;
    private boolean eha_habilitada;
    private String eha_vto_hab;
    private int eha_rev_id; //ID REVISOR
    private boolean eha_activo;
}
