package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.EmpresaHabilitacion;
import com.maticolque.apirestelevadores.model.Revisor;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaHabilitacionDTO {

    private int eha_id;
    private String eha_fecha;
    private EmpresaDTO empresa; // DETALLES DE LA EMPRESA
    private String eha_expediente;
    private boolean eha_habilitada;
    private String eha_vto_hab;
    private RevisorDTO revisor; // DETALLES DEL REVISOR
    private boolean eha_activo;

    public static EmpresaHabilitacion toEntity(EmpresaHabilitacionDTO dto, Empresa empresa, Revisor revisor) {
        EmpresaHabilitacion empresaHabilitacion = new EmpresaHabilitacion();
        empresaHabilitacion.setEha_id(dto.getEha_id());
        empresaHabilitacion.setEha_fecha(LocalDate.parse(dto.getEha_fecha())); // Convertir String a LocalDate si es necesario
        empresaHabilitacion.setEmpresa(empresa);
        empresaHabilitacion.setEha_expediente(dto.getEha_expediente());
        empresaHabilitacion.setEha_habilitada(dto.isEha_habilitada());
        empresaHabilitacion.setEha_vto_hab(LocalDate.parse(dto.getEha_vto_hab())); // Convertir String a LocalDate si es necesario
        empresaHabilitacion.setRevisor(revisor);
        empresaHabilitacion.setEha_activo(dto.isEha_activo());
        return empresaHabilitacion;
    }

    public static EmpresaHabilitacionDTO fromEntity(EmpresaHabilitacion empresaHabilitacion) {
        EmpresaHabilitacionDTO dto = new EmpresaHabilitacionDTO();
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
