package com.maticolque.apirestelevadores.dto;


import com.maticolque.apirestelevadores.model.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabilitacionDocumentoCreateDTO {

    private int hdo_id;
    private int hdo_eha_id; // ID EMPRESA HABILITACION
    private int hdo_tad_id; // ID TIPO ADJUNTO
    private int hdo_adjunto_orden;
    private String hdo_adjunto_fecha;
    private int hdo_rev_id; // ID REVISOR

    public static HabilitacionDocumento toEntity(HabilitacionDocumentoCreateDTO dto, EmpresaHabilitacion empresaHabilitacion, TipoAdjunto tipoAdjunto, Revisor revisor) {
        HabilitacionDocumento habilitacionDocumento = new HabilitacionDocumento();
        habilitacionDocumento.setEmpresaHabilitacion(empresaHabilitacion);
        habilitacionDocumento.setTipoAdjunto(tipoAdjunto);
        habilitacionDocumento.setHdo_adjunto_orden(dto.getHdo_adjunto_orden());
        habilitacionDocumento.setHdo_adjunto_fecha(LocalDate.parse(dto.getHdo_adjunto_fecha()));
        habilitacionDocumento.setRevisor(revisor);
        return habilitacionDocumento;
    }
}
