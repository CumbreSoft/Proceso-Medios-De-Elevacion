package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.HabilitacionDocumento;
import com.maticolque.apirestelevadores.model.MedioDocumento;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabilitacionDocumentoReadDTO {

    private int hdo_id;
    private EmpresaHabilitacionDTO empresaHabilitacion; // DETALLES DE LA EMPRESA HABILITACION
    private TipoAdjuntoDTO tipoAdjunto; // DETALLES DEL TIPO ADJUNTO
    private int hdo_adjunto_orden;
    private String hdo_adjunto_fecha;
    private RevisorDTO revisorInterno; // DETALLES DEL REVISOR

    public static HabilitacionDocumentoReadDTO fromEntity(HabilitacionDocumento habilitacionDocumento) {
        HabilitacionDocumentoReadDTO dto = new HabilitacionDocumentoReadDTO();
        dto.setHdo_id(habilitacionDocumento.getHdo_id());
        dto.setEmpresaHabilitacion(EmpresaHabilitacionDTO.fromEntity(habilitacionDocumento.getEmpresaHabilitacion()));
        dto.setTipoAdjunto(TipoAdjuntoDTO.fromEntity(habilitacionDocumento.getTipoAdjunto()));
        dto.setHdo_adjunto_orden(habilitacionDocumento.getHdo_adjunto_orden());
        dto.setHdo_adjunto_fecha(habilitacionDocumento.getHdo_adjunto_fecha().toString());
        dto.setRevisorInterno(RevisorDTO.fromEntity(habilitacionDocumento.getRevisor()));

        return dto;
    }
}
