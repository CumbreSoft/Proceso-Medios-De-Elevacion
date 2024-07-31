package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.TipoMaquina;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TipoMaquinaDTO {

    private int tma_id;
    private int tma_cod;
    private String tma_detalle;
    private boolean tma_activa;

    //Metodos static para acceder a estos métodos sin tener que crear una instancia de la clase DTO
    public static TipoMaquinaDTO fromEntity(TipoMaquina tipoMaquina) {
        TipoMaquinaDTO dto = new TipoMaquinaDTO();
        dto.setTma_id(tipoMaquina.getTma_id());
        dto.setTma_cod(tipoMaquina.getTma_cod());
        dto.setTma_detalle(tipoMaquina.getTma_detalle());
        dto.setTma_activa(tipoMaquina.isTma_activa());

        return dto;
    }

    public static TipoMaquina toEntity(TipoMaquinaDTO dto) {
        TipoMaquina tipoMaquina = new TipoMaquina();
        tipoMaquina.setTma_id(dto.getTma_id());
        tipoMaquina.setTma_cod(dto.getTma_cod());
        tipoMaquina.setTma_detalle(dto.getTma_detalle());
        tipoMaquina.setTma_activa(dto.isTma_activa());

        return tipoMaquina;
    }
}
