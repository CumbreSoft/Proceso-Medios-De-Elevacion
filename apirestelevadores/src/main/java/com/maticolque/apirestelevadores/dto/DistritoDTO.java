package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Distrito;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistritoDTO {

    private int dis_id;
    private String dis_codigo;
    private String dis_nombre;
    private boolean dis_activo;

    public static DistritoDTO fromEntity(Distrito distrito) {
        DistritoDTO dto = new DistritoDTO();
        dto.setDis_id(distrito.getDis_id());
        dto.setDis_codigo(distrito.getDis_codigo());
        dto.setDis_nombre(distrito.getDis_nombre());
        dto.setDis_activo(distrito.isDis_activo());
        return dto;
    }

    public static Distrito toEntity(DistritoDTO dto) {
        Distrito distrito = new Distrito();
        distrito.setDis_id(dto.getDis_id());
        distrito.setDis_codigo(dto.getDis_codigo());
        distrito.setDis_nombre(dto.getDis_nombre());
        distrito.setDis_activo(dto.isDis_activo());
        return distrito;
    }
}
