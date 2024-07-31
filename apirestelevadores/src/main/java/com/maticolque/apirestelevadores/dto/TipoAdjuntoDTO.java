package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.TipoAdjunto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoAdjuntoDTO {

    private int tad_id;
    private String tad_nombre;
    private int tad_cod;
    private boolean tad_activo;

    //Metodos static para acceder a estos métodos sin tener que crear una instancia de la clase DTO
    public static TipoAdjuntoDTO fromEntity(TipoAdjunto tipoAdjunto) {
        TipoAdjuntoDTO dto = new TipoAdjuntoDTO();
        dto.setTad_id(tipoAdjunto.getTad_id());
        dto.setTad_nombre(tipoAdjunto.getTad_nombre());
        dto.setTad_cod(tipoAdjunto.getTad_cod());
        dto.setTad_activo(tipoAdjunto.isTad_activo());

        return dto;
    }

    public static TipoAdjunto toEntity(TipoAdjuntoDTO dto) {
        TipoAdjunto tipoAdjunto = new TipoAdjunto();
        tipoAdjunto.setTad_id(dto.getTad_id());
        tipoAdjunto.setTad_nombre(dto.getTad_nombre());
        tipoAdjunto.setTad_cod(dto.getTad_cod());
        tipoAdjunto.setTad_activo(dto.isTad_activo());

        return tipoAdjunto;
    }
}
