package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_medioselevacion")
public class MedioElevacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mde_id")
    private int mde_id;

    @OneToOne
    @JoinColumn(name = "mde_tma_id", nullable = false)
    private TipoMaquina tiposMaquinas;

    @Column(name = "mde_ubicacion", nullable = false, length = 30)
    private String mde_ubicacion;

    @Column(name = "mde_tipo", nullable = false, length = 25)
    private String mde_tipo;

    @Column(name = "mde_niveles", nullable = false)
    private int mde_niveles;

    @Column(name = "mde_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mde_activo;

}
