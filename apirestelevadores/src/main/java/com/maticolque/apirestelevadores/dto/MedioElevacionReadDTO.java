package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.MedioElevacion;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MedioElevacionReadDTO {

    private int mde_id;
    private TipoMaquinaDTO tiposMaquina; // Detalles del Tipo Maquina
    private String mde_ubicacion;
    private String mde_tipo;
    private int mde_niveles;
    private boolean mde_planos_aprob;
    private String mde_expte_planos;
    private EmpresaDTO empresa; // Detalles de la Empresa
    private boolean mde_activo;

    public static MedioElevacionReadDTO fromEntity(MedioElevacion medioElevacion) {
        MedioElevacionReadDTO dto = new MedioElevacionReadDTO();
        dto.setMde_id(medioElevacion.getMde_id());
        dto.setTiposMaquina(TipoMaquinaDTO.fromEntity(medioElevacion.getTiposMaquinas()));
        dto.setMde_ubicacion(medioElevacion.getMde_ubicacion());
        dto.setMde_tipo(medioElevacion.getMde_tipo());
        dto.setMde_niveles(medioElevacion.getMde_niveles());
        dto.setMde_planos_aprob(medioElevacion.isMde_planos_aprob());
        dto.setMde_expte_planos(medioElevacion.getMde_expte_planos());
        dto.setEmpresa(medioElevacion.getEmpresa() != null ? EmpresaDTO.fromEntity(medioElevacion.getEmpresa()) : null);
        dto.setMde_activo(medioElevacion.isMde_activo());
        return dto;
    }
}
