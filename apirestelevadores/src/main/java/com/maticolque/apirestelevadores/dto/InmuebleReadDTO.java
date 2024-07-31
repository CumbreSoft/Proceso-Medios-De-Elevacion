package com.maticolque.apirestelevadores.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maticolque.apirestelevadores.model.Inmueble;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleReadDTO {

    private int inm_id;
    private int inm_padron;
    private String inm_direccion;
    private String inm_cod_postal;
    private DistritoDTO distrito; // DTO de Distrito para incluir detalles en la respuesta
    private DestinoDTO destino; // DTO de Destino para incluir detalles en la respuesta
    private boolean inm_activo;

    public static InmuebleReadDTO fromEntity(Inmueble inmueble) {
        InmuebleReadDTO dto = new InmuebleReadDTO();
        dto.setInm_id(inmueble.getInm_id());
        dto.setInm_padron(inmueble.getInm_padron());
        dto.setInm_direccion(inmueble.getInm_direccion());
        dto.setInm_cod_postal(inmueble.getInm_cod_postal());
        dto.setDistrito(DistritoDTO.fromEntity(inmueble.getDistrito()));
        dto.setDestino(DestinoDTO.fromEntity(inmueble.getDestino()));
        dto.setInm_activo(inmueble.isInm_activo());
        return dto;
    }
}
