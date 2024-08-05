package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioHabilitacionDTO {

    private int mha_id;
    private int mha_inm_padron_guardado;
    private MedioElevacionDTO medioElevacion; // DETALLES DEL MDE
    private EmpresaDTO nuevaEmpresa; // DETALLES DE LA EMPRESA
    private PersonaDTO persona; // DETALLES DE LA PERSONA
    private String mha_fecha;
    private String mha_expediente;
    private String mha_fecha_vto;
    private String mha_fecha_pago;
    private String mha_fecha_inspec;
    private boolean mha_habilitado;
    private boolean mha_oblea_entregada;
    private boolean mha_activo;
    private RevisorDTO revisor; // DETALLES DEL REVISOR


    public static MedioHabilitacionDTO fromEntity(MedioHabilitacion medioHabilitacion) {
        MedioHabilitacionDTO dto = new MedioHabilitacionDTO();
        dto.setMha_id(medioHabilitacion.getMha_id());
        dto.setMha_inm_padron_guardado(medioHabilitacion.getMha_inm_padron_guardado());
        dto.setMedioElevacion(MedioElevacionDTO.fromEntity(medioHabilitacion.getMedioElevacion())); //MDE
        //dto.setEmpresa(EmpresaDTO.fromEntity(medioHabilitacion.getEmpresa())); // Datos Empresa
        dto.setNuevaEmpresa(medioHabilitacion.getEmpresa() != null ? EmpresaDTO.fromEntity(medioHabilitacion.getEmpresa()) : null);
        dto.setPersona(PersonaDTO.fromEntity(medioHabilitacion.getPersona())); //PERSONA
        dto.setMha_fecha(medioHabilitacion.getMha_fecha().toString());
        dto.setMha_expediente(medioHabilitacion.getMha_expediente());
        dto.setMha_fecha_vto(medioHabilitacion.getMha_fecha_vto().toString());
        dto.setMha_fecha_pago(medioHabilitacion.getMha_fecha_pago().toString());
        dto.setMha_fecha_inspec(medioHabilitacion.getMha_fecha_inspec().toString());
        dto.setMha_habilitado(medioHabilitacion.isMha_habilitado());
        dto.setMha_oblea_entregada(medioHabilitacion.isMha_oblea_entregada());
        dto.setMha_activo(medioHabilitacion.isMha_activo());
        dto.setRevisor(RevisorDTO.fromEntity(medioHabilitacion.getRevisor())); //REVISOR
        return dto;
    }

    public static MedioHabilitacion toEntity(MedioHabilitacionDTO dto, MedioElevacion medioElevacion, Empresa empresa, Persona persona, Revisor revisor) {
        MedioHabilitacion medioHabilitacion = new MedioHabilitacion();
        medioHabilitacion.setMha_id(dto.getMha_id());
        medioHabilitacion.setMha_inm_padron_guardado(dto.getMha_inm_padron_guardado());
        medioHabilitacion.setMedioElevacion(medioElevacion); // MDE

        // Solo asignar la empresa si no es null
        if (dto.getNuevaEmpresa().getEmp_id() != 0) {
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
