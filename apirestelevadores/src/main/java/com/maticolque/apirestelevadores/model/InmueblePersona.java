package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_inmuebles_personas")
public class InmueblePersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipe_id")
    private int ipe_id;

    @OneToOne
    @JoinColumn(name = "ipe_inm_id", nullable = false)
    private Inmueble inmueble;

    @OneToOne
    @JoinColumn(name = "ipe_per_id", nullable = false)
    private Persona persona;

    @Column(name = "ipe_es_admin_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ipe_es_admin_edif;

    @Column(name = "ipe_es_coprop_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ipe_es_coprop_edif;


}
