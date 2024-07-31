package com.maticolque.apirestelevadores.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioHabilitacionUpdateDTO {

    private int mha_inm_padron_guardado;
    private int mha_mde_id; // ID MDE
    private int mha_emp_id; // ID EMPRESA
    private int mha_per_id; // ID PERSONA
    private String mha_fecha;
    private String mha_expediente;
    private String mha_fecha_vto;
    private String mha_fecha_pago;
    private String mha_fecha_inspec;
    private boolean mha_habilitado;
    private boolean mha_oblea_entregada;
    private int mha_rev_id; // ID REVISOR
    private boolean mha_activo;
}
