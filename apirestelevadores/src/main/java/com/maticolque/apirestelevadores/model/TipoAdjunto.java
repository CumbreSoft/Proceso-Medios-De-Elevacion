package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_tipos_adjuntos")
public class TipoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tad_id")
    private int tad_id;

    @Column(name = "tad_nombre", nullable = false, length = 30)
    private String tad_nombre;

    @Column(name = "tad_cod", nullable = false)
    private int tad_cod;

    @Column(name = "tad_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tad_activo;
}
