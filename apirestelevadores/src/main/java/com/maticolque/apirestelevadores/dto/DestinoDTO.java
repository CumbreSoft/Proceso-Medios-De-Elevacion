package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DestinoDTO {

    private int dst_id;
    private String dst_codigo;
    private String dst_detalle;
    private boolean dst_activo;

    public static DestinoDTO fromEntity(Destino destino) {
        DestinoDTO dto = new DestinoDTO();
        dto.setDst_id(destino.getDst_id());
        dto.setDst_codigo(destino.getDst_codigo());
        dto.setDst_detalle(destino.getDst_detalle());
        dto.setDst_activo(destino.isDst_activo());
        return dto;
    }

    public static Destino toEntity(DestinoDTO dto) {
        Destino destino = new Destino();
        destino.setDst_id(dto.getDst_id());
        destino.setDst_codigo(dto.getDst_codigo());
        destino.setDst_detalle(dto.getDst_detalle());
        destino.setDst_activo(dto.isDst_activo());
        return destino;
    }
}
