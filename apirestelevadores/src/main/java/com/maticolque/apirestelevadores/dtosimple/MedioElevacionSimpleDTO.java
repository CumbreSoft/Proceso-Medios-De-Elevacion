package com.maticolque.apirestelevadores.dtosimple;

import com.maticolque.apirestelevadores.model.MedioElevacion;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedioElevacionSimpleDTO {

    private int mde_id;
    private TiposMaquinasSimpleDTO tiposMaquinas;
    private String mde_ubicacion;
    private EmpresaPersonaSimpleDTO empresa; // Detalles de la Empresa


    public static MedioElevacionSimpleDTO fromEntity(MedioElevacion medioElevacion, EmpresaPersonaSimpleDTO empresaPersonaSimpleDTO) {
        MedioElevacionSimpleDTO dto = new MedioElevacionSimpleDTO();
        dto.setMde_id(medioElevacion.getMde_id());
        dto.setTiposMaquinas(TiposMaquinasSimpleDTO.fromEntity(medioElevacion.getTiposMaquinas()));
        dto.setMde_ubicacion(medioElevacion.getMde_ubicacion());
        dto.setEmpresa(empresaPersonaSimpleDTO);
        return dto;
    }
}
