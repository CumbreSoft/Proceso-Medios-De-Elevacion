package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.MedioDocumento;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MedioDocumentoReadDTO {

    private int mdo_id;
    private MedioHabilitacionDTO medioHabilitacion; // DETALLES DEL MEDIO HABILITACION
    private TipoAdjuntoDTO tipoAdjunto; // DETALLES DEL TIPO ADJUNTO
    private int mdo_adjunto_orden;
    private String mdo_adjunto_fecha;
    private RevisorDTO revisorDocumento; // DETALLES DEL REVISOR

    public static MedioDocumentoReadDTO fromEntity(MedioDocumento medioDocumento) {
        MedioDocumentoReadDTO dto = new MedioDocumentoReadDTO();
        dto.setMdo_id(medioDocumento.getMdo_id());
        dto.setMedioHabilitacion(MedioHabilitacionDTO.fromEntity(medioDocumento.getMedioHabilitacion()));
        dto.setTipoAdjunto(TipoAdjuntoDTO.fromEntity(medioDocumento.getTipoAdjunto()));
        dto.setMdo_adjunto_orden(medioDocumento.getMdo_adjunto_orden());
        dto.setMdo_adjunto_fecha(medioDocumento.getMdo_adjunto_fecha().toString());
        dto.setRevisorDocumento(RevisorDTO.fromEntity(medioDocumento.getRevisor()));

        return dto;
    }
}
