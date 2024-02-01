package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_tipos_maquinas")
public class TipoMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tma_id")
    private int tma_id;

    @Column(name = "tma_cod", nullable = false)
    private Integer tma_cod;

    @Column(name = "tma_detalle", length = 30)
    private String tma_detalle;

    @Column(name = "tma_activa", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tma_activa;

}
