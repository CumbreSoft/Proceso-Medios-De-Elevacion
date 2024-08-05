package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.EmpresaPersona;
import com.maticolque.apirestelevadores.model.InmueblePersona;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaEmpresaInmuebleDTO {

    private int per_id;
    private Integer epe_id; // Puede ser null si no hay empresa
    private Integer ipe_id; // Puede ser null si no hay inmueble
    private PersonaDTO persona;
    private EmpresaDTO empresa;
    private InmuebleDTO inmueble;

    // Getters y Setters

    public static PersonaEmpresaInmuebleDTO fromEmpresaPersona(EmpresaPersona empresaPersona) {
        PersonaEmpresaInmuebleDTO dto = new PersonaEmpresaInmuebleDTO();
        dto.setPer_id(empresaPersona.getPersona().getPer_id());
        dto.setEpe_id(empresaPersona.getEpe_id());
        dto.setPersona(PersonaDTO.fromEntity(empresaPersona.getPersona()));
        dto.setEmpresa(EmpresaDTO.fromEntity(empresaPersona.getEmpresa()));
        dto.setInmueble(null); // No hay inmueble en este caso
        return dto;
    }

    public static PersonaEmpresaInmuebleDTO fromInmueblePersona(InmueblePersona inmueblePersona) {
        PersonaEmpresaInmuebleDTO dto = new PersonaEmpresaInmuebleDTO();
        dto.setPer_id(inmueblePersona.getPersona().getPer_id());
        dto.setIpe_id(inmueblePersona.getIpe_id());
        dto.setPersona(PersonaDTO.fromEntity(inmueblePersona.getPersona()));
        dto.setInmueble(InmuebleDTO.fromEntity(inmueblePersona.getInmueble()));
        dto.setEmpresa(null); // No hay empresa en este caso
        return dto;
    }
}
