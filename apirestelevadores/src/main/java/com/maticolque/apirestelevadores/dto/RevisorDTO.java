package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Empresa;
import com.maticolque.apirestelevadores.model.Revisor;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevisorDTO {

    private int rev_id;
    private String rev_apellido;
    private String rev_nombre;
    private String rev_cuit;
    private int rev_tipodoc;
    private String rev_numdoc;
    private String rev_correo;
    private String rev_telefono;
    private String rev_usuario_sayges;
    private boolean rev_aprob_mde;
    private boolean rev_renov_mde;
    private boolean rev_aprob_emp;
    private boolean rev_activo;

    public static RevisorDTO fromEntity(Revisor revisor) {
        RevisorDTO dto = new RevisorDTO();
        dto.setRev_id(revisor.getRev_id());
        dto.setRev_apellido(revisor.getRev_apellido());
        dto.setRev_nombre(revisor.getRev_nombre());
        dto.setRev_cuit(revisor.getRev_cuit());
        dto.setRev_tipodoc(revisor.getRev_tipodoc());
        dto.setRev_numdoc(revisor.getRev_numdoc());
        dto.setRev_correo(revisor.getRev_correo());
        dto.setRev_telefono(revisor.getRev_telefono());
        dto.setRev_usuario_sayges(revisor.getRev_usuario_sayges());
        dto.setRev_aprob_mde(revisor.isRev_aprob_mde());
        dto.setRev_renov_mde(revisor.isRev_renov_mde());
        dto.setRev_aprob_emp(revisor.isRev_aprob_emp());
        dto.setRev_activo(revisor.isRev_activo());

        return dto;
    }

    public static Revisor toEntity(RevisorDTO dto) {
        Revisor revisor = new Revisor();
        revisor.setRev_id(dto.getRev_id());
        revisor.setRev_apellido(dto.getRev_apellido());
        revisor.setRev_nombre(dto.getRev_nombre());
        revisor.setRev_cuit(dto.getRev_cuit());
        revisor.setRev_tipodoc(dto.getRev_tipodoc());
        revisor.setRev_numdoc(dto.getRev_numdoc());
        revisor.setRev_correo(dto.getRev_correo());
        revisor.setRev_telefono(dto.getRev_telefono());
        revisor.setRev_usuario_sayges(dto.getRev_usuario_sayges());
        revisor.setRev_aprob_mde(dto.isRev_aprob_mde());
        revisor.setRev_renov_mde(dto.isRev_renov_mde());
        revisor.setRev_aprob_emp(dto.isRev_aprob_emp());
        revisor.setRev_activo(dto.isRev_activo());

        return revisor;
    }
}
