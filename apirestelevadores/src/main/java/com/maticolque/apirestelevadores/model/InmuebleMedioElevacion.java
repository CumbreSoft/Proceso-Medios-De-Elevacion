package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_inmuebles_medioselevacion")
public class InmuebleMedioElevacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ime_id")
    private int ime_id;

    @ManyToOne
    @JoinColumn(name = "ime_inm_id", referencedColumnName = "inm_id", nullable = false)
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "ime_mde_id", referencedColumnName = "mde_id", nullable = false)
    private MedioElevacion medioElevacion;

}
