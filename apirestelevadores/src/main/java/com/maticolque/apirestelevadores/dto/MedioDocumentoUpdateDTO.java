package com.maticolque.apirestelevadores.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioDocumentoUpdateDTO {

    private int mdo_tad_id; // ID TIPO ADJUNTO
    private int mdo_adjunto_orden;
    private String mdo_adjunto_fecha;
    private int mdo_rev_id; // ID REVISOR
}
