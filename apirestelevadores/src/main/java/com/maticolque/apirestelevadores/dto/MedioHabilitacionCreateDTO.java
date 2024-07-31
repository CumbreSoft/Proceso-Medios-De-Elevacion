package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MedioHabilitacionCreateDTO {

    private int mha_id;
    private int mha_inm_padron_guardado;
    private int mha_mde_id; // ID MDE
    private int mha_emp_id; // ID EMPRESA
    private int mha_per_id; // ID PERSONA
    private String mha_fecha;
    private String mha_expediente;
    private String mha_fecha_vto;
    private String mha_fecha_pago;
    private String mha_fecha_inspec;
    private boolean mha_habilitado;
    private boolean mha_oblea_entregada;
    private boolean mha_activo;
    private int mha_rev_id; // ID REVISOR

    public static MedioHabilitacion toEntity(MedioHabilitacionCreateDTO dto, MedioElevacion medioElevacion, Empresa empresa, Persona persona, Revisor revisor) {
        MedioHabilitacion medioHabilitacion = new MedioHabilitacion();
        medioHabilitacion.setMha_id(dto.getMha_id());
        medioHabilitacion.setMha_inm_padron_guardado(dto.getMha_inm_padron_guardado());
        medioHabilitacion.setMedioElevacion(medioElevacion); // MDE

        // Solo asignar la empresa si no es null
        if (dto.getMha_emp_id() != 0) {
            medioHabilitacion.setEmpresa(empresa); // Asignar empresa si ID es distinto de 0
        } else {
            medioHabilitacion.setEmpresa(null); // Si el ID es 0, establecer como null
        }

        medioHabilitacion.setPersona(persona); // PERSONA
        medioHabilitacion.setMha_fecha(LocalDate.parse(dto.getMha_fecha()));
        medioHabilitacion.setMha_expediente(dto.getMha_expediente());
        medioHabilitacion.setMha_fecha_vto(LocalDate.parse(dto.getMha_fecha_vto()));
        medioHabilitacion.setMha_fecha_pago(LocalDate.parse(dto.getMha_fecha_pago()));
        medioHabilitacion.setMha_fecha_inspec(LocalDate.parse(dto.getMha_fecha_inspec()));
        medioHabilitacion.setMha_habilitado(dto.isMha_habilitado());
        medioHabilitacion.setMha_oblea_entregada(dto.isMha_oblea_entregada());
        medioHabilitacion.setMha_activo(dto.isMha_activo());
        medioHabilitacion.setRevisor(revisor); // REVISOR
        return medioHabilitacion;
    }

}
