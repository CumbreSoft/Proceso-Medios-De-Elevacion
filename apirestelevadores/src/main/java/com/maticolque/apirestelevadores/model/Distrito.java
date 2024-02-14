package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_distrito")
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id")
    private int dis_id;

    @Column(name = "dis_codigo", nullable = false, length = 2)
    private String dis_codigo;

    @Column(name = "dis_nombre", nullable = false, length = 30)
    private String dis_nombre;

    @Column(name = "dis_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dis_activo;
}
