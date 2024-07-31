package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.Persona;
import com.maticolque.apirestelevadores.model.Revisor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MedioHabilitacionResponse {
    private int mha_id;
    private int mha_inm_padron_guardado;
    private MedioElevacion medioElevacion;
    private Empresa empresa; // Esta se incluye solo si el Medio de Elevación no tiene una empresa asociada
    private Persona persona;
    private String mha_fecha;
    private String mha_expediente;
    private String mha_fecha_vto;
    private String mha_fecha_pago;
    private String mha_fecha_inspec;
    private boolean mha_habilitado;
    private boolean mha_oblea_entregada;
    private boolean mha_activo;
    private Revisor revisor;
}
