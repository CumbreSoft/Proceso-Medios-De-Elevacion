package com.maticolque.apirestelevadores.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleUpdateDTO {

    private int inm_padron;
    private String inm_direccion;
    private String inm_cod_postal;
    private int inm_dis_id; // ID del Distrito
    private int inm_dst_id; // ID del Destino
    private boolean inm_activo;
}
