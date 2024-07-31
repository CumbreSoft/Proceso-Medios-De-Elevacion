package com.maticolque.apirestelevadores.dtosimple;

import com.maticolque.apirestelevadores.model.TipoMaquina;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TiposMaquinasSimpleDTO {

    private int tma_id;
    private String tma_detalle;

    public static TiposMaquinasSimpleDTO fromEntity(TipoMaquina tipoMaquina) {
        TiposMaquinasSimpleDTO dto = new TiposMaquinasSimpleDTO();
        dto.setTma_id(tipoMaquina.getTma_id());
        dto.setTma_detalle(tipoMaquina.getTma_detalle());
        return dto;
    }
}
