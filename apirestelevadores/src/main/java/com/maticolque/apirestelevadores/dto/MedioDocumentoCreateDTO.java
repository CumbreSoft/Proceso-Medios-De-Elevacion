package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioDocumentoCreateDTO {

    private int mdo_id;
    private int mdo_mha_id; // ID MEDIO HABILITACION
    private int mdo_tad_id; // ID TIPO ADJUNTO
    private int mdo_adjunto_orden;
    private String mdo_adjunto_fecha;
    private int mdo_rev_id; // ID REVISOR

    public static MedioDocumento toEntity(MedioDocumentoCreateDTO dto, MedioHabilitacion medioHabilitacion, TipoAdjunto tipoAdjunto, Revisor revisor) {
        MedioDocumento medioDocumento = new MedioDocumento();
        medioDocumento.setMedioHabilitacion(medioHabilitacion);
        medioDocumento.setTipoAdjunto(tipoAdjunto);
        medioDocumento.setMdo_adjunto_orden(dto.getMdo_adjunto_orden());
        medioDocumento.setMdo_adjunto_fecha(LocalDate.parse(dto.getMdo_adjunto_fecha()));
        medioDocumento.setRevisor(revisor);
        return medioDocumento;
    }

}
