package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioElevacionDTO {

    private int mde_id;
    private TipoMaquinaDTO tiposMaquina;
    private String mde_ubicacion;
    private String mde_tipo;
    private int mde_niveles;
    private boolean mde_planos_aprob;
    private String mde_expte_planos;
    private EmpresaDTO empresa;
    private boolean mde_activo;

    public static MedioElevacionDTO fromEntity(MedioElevacion medioElevacion) {
        MedioElevacionDTO dto = new MedioElevacionDTO();
        dto.setMde_id(medioElevacion.getMde_id());
        dto.setTiposMaquina(TipoMaquinaDTO.fromEntity(medioElevacion.getTiposMaquinas()));
        dto.setMde_ubicacion(medioElevacion.getMde_ubicacion());
        dto.setMde_tipo(medioElevacion.getMde_tipo());
        dto.setMde_niveles(medioElevacion.getMde_niveles());
        dto.setMde_planos_aprob(medioElevacion.isMde_planos_aprob());
        dto.setMde_expte_planos(medioElevacion.getMde_expte_planos());
        if (medioElevacion.getEmpresa() != null) {
            dto.setEmpresa(EmpresaDTO.fromEntity(medioElevacion.getEmpresa()));
        } else {
            dto.setEmpresa(null);
        }
        dto.setMde_activo(medioElevacion.isMde_activo());
        return dto;
    }

    public static MedioElevacion toEntity(MedioElevacion dto, TipoMaquina tipoMaquina, Empresa empresa) {
        MedioElevacion medioElevacion = new MedioElevacion();
        medioElevacion.setMde_id(dto.getMde_id());
        medioElevacion.setTiposMaquinas(tipoMaquina);
        medioElevacion.setMde_ubicacion(dto.getMde_ubicacion());
        medioElevacion.setMde_tipo(dto.getMde_tipo());
        medioElevacion.setMde_niveles(dto.getMde_niveles());
        medioElevacion.setMde_planos_aprob(dto.isMde_planos_aprob());
        medioElevacion.setMde_expte_planos(dto.getMde_expte_planos());
        medioElevacion.setEmpresa(empresa);
        medioElevacion.setMde_activo(dto.isMde_activo());
        return medioElevacion;
    }
}
