package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InmuebleCreateDTO {

    private int inm_id;
    private int inm_padron;
    private String inm_direccion;
    private String inm_cod_postal;
    private int inm_dis_id; //ID de Distrito;
    private int inm_dst_id; //ID de Destino;
    private boolean inm_activo;

    public static Inmueble toEntity(InmuebleCreateDTO dto, Distrito distrito, Destino destino) {

        Inmueble inmueble = new Inmueble();
        inmueble.setInm_id(dto.getInm_id());
        inmueble.setInm_padron(dto.getInm_padron());
        inmueble.setInm_direccion(dto.getInm_direccion());
        inmueble.setInm_cod_postal(dto.getInm_cod_postal());
        inmueble.setDistrito(distrito);
        inmueble.setDestino(destino);
        inmueble.setInm_activo(dto.isInm_activo());
        return inmueble;
    }
}
