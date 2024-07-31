package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Empresa;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO {

    private int emp_id;
    private String emp_razon;
    private String emp_cuit;
    private String emp_domic_legal;
    private String emp_telefono;
    private String emp_correo;
    private boolean emp_activa;


    //Metodos static para acceder a estos métodos sin tener que crear una instancia de la clase DTO
    public static EmpresaDTO fromEntity(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setEmp_id(empresa.getEmp_id());
        dto.setEmp_razon(empresa.getEmp_razon());
        dto.setEmp_cuit(empresa.getEmp_cuit());
        dto.setEmp_domic_legal(empresa.getEmp_domic_legal());
        dto.setEmp_telefono(empresa.getEmp_telefono());
        dto.setEmp_correo(empresa.getEmp_correo());
        dto.setEmp_activa(empresa.isEmp_activa());
        return dto;
    }

    public static Empresa toEntity(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setEmp_id(dto.getEmp_id());
        empresa.setEmp_razon(dto.getEmp_razon());
        empresa.setEmp_cuit(dto.getEmp_cuit());
        empresa.setEmp_domic_legal(dto.getEmp_domic_legal());
        empresa.setEmp_telefono(dto.getEmp_telefono());
        empresa.setEmp_correo(dto.getEmp_correo());
        empresa.setEmp_activa(dto.isEmp_activa());
        return empresa;
    }

}
