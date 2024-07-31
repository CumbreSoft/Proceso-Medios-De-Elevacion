package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InmueblePersonaUpdateDTO {

    private boolean ipe_es_admin_edif;
    private boolean ipe_es_coprop_edif;
    private int ipe_inm_id; //ID de Inmueble
}
