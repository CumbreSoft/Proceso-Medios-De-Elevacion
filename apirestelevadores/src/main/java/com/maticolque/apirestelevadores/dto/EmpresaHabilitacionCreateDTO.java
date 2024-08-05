package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaHabilitacionCreateDTO {

    private int eha_id;
    private String eha_fecha;
    private int eha_emp_id; // ID EMPRESA
    private String eha_expediente;
    private boolean eha_habilitada;
    private String eha_vto_hab;
    private int eha_rev_id; // ID REVISOR
    private boolean eha_activo;

    public static EmpresaHabilitacion toEntity(EmpresaHabilitacionCreateDTO dto, Empresa empresa, Revisor revisor) {
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
}
