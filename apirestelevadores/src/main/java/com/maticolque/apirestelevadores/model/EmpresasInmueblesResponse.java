package com.maticolque.apirestelevadores.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class EmpresasInmueblesResponse {
    private List<EmpresaPersona> empresaPersonas;
    private List<InmueblePersona> inmueblePersonas;

    public EmpresasInmueblesResponse(List<EmpresaPersona> empresaPersonas, List<InmueblePersona> inmueblePersonas) {
        this.empresaPersonas = empresaPersonas;
        this.inmueblePersonas = inmueblePersonas;
    }
}
