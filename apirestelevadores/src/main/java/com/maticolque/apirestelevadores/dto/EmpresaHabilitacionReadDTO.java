package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaHabilitacionReadDTO {

    private int eha_id;
    private String eha_fecha;
    private EmpresaDTO empresa; // Detalles de la Empresa
    private String eha_expediente;
    private boolean eha_habilitada;
    private String eha_vto_hab;
    private RevisorDTO revisor; // Detalles del Revisor
    private boolean eha_activo;

    public static EmpresaHabilitacionReadDTO fromEntity(EmpresaHabilitacion empresaHabilitacion) {
        EmpresaHabilitacionReadDTO dto = new EmpresaHabilitacionReadDTO();
        dto.setEha_id(empresaHabilitacion.getEha_id());
        dto.setEha_fecha(empresaHabilitacion.getEha_fecha().toString()); // Convertir LocalDate a String si es necesario
        dto.setEmpresa(EmpresaDTO.fromEntity(empresaHabilitacion.getEmpresa())); // Datos Empresa
        dto.setEha_expediente(empresaHabilitacion.getEha_expediente());
        dto.setEha_habilitada(empresaHabilitacion.isEha_habilitada());
        dto.setEha_vto_hab(empresaHabilitacion.getEha_vto_hab().toString()); // Convertir LocalDate a String si es necesario
        dto.setRevisor(RevisorDTO.fromEntity(empresaHabilitacion.getRevisor())); // Datos Revisor
        dto.setEha_activo(empresaHabilitacion.isEha_activo());
        return dto;
    }

}
