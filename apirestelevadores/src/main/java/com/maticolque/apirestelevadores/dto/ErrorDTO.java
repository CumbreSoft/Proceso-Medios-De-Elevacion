package com.maticolque.apirestelevadores.dto;

import lombok.*;

@Data
@Builder

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorDTO {
    private  String code;
    private  String message;
}

