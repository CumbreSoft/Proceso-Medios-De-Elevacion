package com.maticolque.apirestelevadores.dto;

import com.maticolque.apirestelevadores.model.Destino;
import com.maticolque.apirestelevadores.model.MedioElevacion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaDTO<T> {
    private T entidad;
    private String message;

    public RespuestaDTO(T entidad, String message) {
        this.entidad = entidad;
        this.message = message;
    }
}