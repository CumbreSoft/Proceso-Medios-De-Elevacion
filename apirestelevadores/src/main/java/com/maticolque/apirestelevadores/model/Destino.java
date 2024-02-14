package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_destinos")
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dst_id")
    private int dst_id;

    @Column(name = "dst_codigo", nullable = false, length = 3)
    private String dst_codigo;

    @Column(name = "dst_detalle", length = 40)
    private String dst_detalle;

    @Column(name = "dst_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dst_activo;

}
