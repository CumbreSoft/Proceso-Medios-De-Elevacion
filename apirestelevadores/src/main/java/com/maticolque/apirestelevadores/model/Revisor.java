package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_revisores")
public class Revisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rev_id")
    private int rev_id;

    @Column(name = "rev_nombre", nullable = false, length = 50)
    private String rev_nombre;

    @Column(name = "rev_numdoc", nullable = false, length = 8)
    private String rev_numdoc;

    @Column(name = "rev_aprob_mde", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_aprob_mde;

    @Column(name = "rev_renov_mde", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_renov_mde;

    @Column(name = "rev_aprob_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_aprob_emp;

    @Column(name = "rev_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_activo;
}
