package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.Distrito;
import com.maticolque.apirestelevadores.model.Inmueble;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleDTO {

    private int inm_id;
    private int inm_padron;
    private String inm_direccion;
    private String inm_cod_postal;
    private DistritoDTO distrito; // DTO de Distrito para incluir detalles en la respuesta
    private DestinoDTO destino; // DTO de Destino para incluir detalles en la respuesta
    private boolean inm_activo;

    public static InmuebleDTO fromEntity(Inmueble inmueble) {
        InmuebleDTO dto = new InmuebleDTO();
        dto.setInm_id(inmueble.getInm_id());
        dto.setInm_padron(inmueble.getInm_padron());
        dto.setInm_direccion(inmueble.getInm_direccion());
        dto.setInm_cod_postal(inmueble.getInm_cod_postal());
        dto.setDistrito(DistritoDTO.fromEntity(inmueble.getDistrito()));
        dto.setDestino(DestinoDTO.fromEntity(inmueble.getDestino()));
        dto.setInm_activo(inmueble.isInm_activo());
        return dto;
    }

    public static Inmueble toEntity(InmuebleDTO dto, Distrito distrito, Destino destino) {
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
