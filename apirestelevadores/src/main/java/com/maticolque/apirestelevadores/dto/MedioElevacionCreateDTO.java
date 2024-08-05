package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import com.maticolque.apirestelevadores.model.TipoMaquina;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MedioElevacionCreateDTO {

    private int mde_id;
    private int mde_tma_id; //ID TIPOMAQUINA
    private String mde_ubicacion;
    private String mde_tipo;
    private int mde_niveles;
    private boolean mde_planos_aprob;
    private String mde_expte_planos;
    private int mde_emp_id; //ID EMPRESA
    private boolean mde_activo;

    public static MedioElevacion toEntity(MedioElevacionCreateDTO dto, TipoMaquina tipoMaquina, Empresa empresa) {
        MedioElevacion medioElevacion = new MedioElevacion();
        medioElevacion.setMde_id(dto.getMde_id());
        medioElevacion.setTiposMaquinas(tipoMaquina);
        medioElevacion.setMde_ubicacion(dto.getMde_ubicacion());
        medioElevacion.setMde_tipo(dto.getMde_tipo());
        medioElevacion.setMde_niveles(dto.getMde_niveles());
        medioElevacion.setMde_planos_aprob(dto.isMde_planos_aprob());
        medioElevacion.setMde_expte_planos(dto.getMde_expte_planos());
        // Solo asignar la empresa si no es null
        if (dto.getMde_emp_id() != 0) {
            medioElevacion.setEmpresa(empresa); // Asignar empresa si ID es distinto de 0
        } else {
            medioElevacion.setEmpresa(null); // Si el ID es 0, establecer como null
        }
        //medioElevacion.setEmpresa(empresa);
        medioElevacion.setMde_activo(dto.isMde_activo());
        return medioElevacion;
    }
}
