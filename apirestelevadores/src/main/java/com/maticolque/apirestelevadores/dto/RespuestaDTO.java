package com.maticolque.apirestelevadores.dto;


import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RespuestaDTO<T> {
    private Map<String, T> entidad;
    private String message;

    public RespuestaDTO(T entidad, String nombreEntidad, String message) {
        this.entidad = new HashMap<>();
        this.entidad.put(nombreEntidad, entidad);
        this.message = message;
    }
}