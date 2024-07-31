package com.maticolque.apirestelevadores.dto;


import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import com.maticolque.apirestelevadores.model.MedioHabilitacion;
import com.maticolque.apirestelevadores.model.Persona;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedioHabilitacionReadDTO {

    private int mha_id;
    private int mha_inm_padron_guardado;
    private MedioElevacionDTO medioElevacion; // Detalles del MDE
    private EmpresaDTO nuevaEmpresa; // Detalles de la Empresa
    private PersonaDTO persona; // Detalles de la Persona
    private String mha_fecha;
    private String mha_expediente;
    private String mha_fecha_vto;
    private String mha_fecha_pago;
    private String mha_fecha_inspec;
    private boolean mha_habilitado;
    private boolean mha_oblea_entregada;
    private boolean mha_activo;
    private RevisorDTO revisor; // Detalles del Revisor

    public static MedioHabilitacionReadDTO fromEntity(MedioHabilitacion medioHabilitacion) {
        MedioHabilitacionReadDTO dto = new MedioHabilitacionReadDTO();
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
}
