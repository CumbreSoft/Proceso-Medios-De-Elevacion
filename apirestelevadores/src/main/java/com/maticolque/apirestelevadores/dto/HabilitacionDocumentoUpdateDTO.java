package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabilitacionDocumentoUpdateDTO {

    private int hdo_tad_id; // ID TIPO ADJUNTO
    private int hdo_adjunto_orden;
    private String hdo_adjunto_fecha;
    private int hdo_rev_id; // ID REVISOR
}
