package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas_personas")
public class EmpresaPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "epe_id")
    private int epe_id;

    @Column(name = "epe_es_dueno_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean epe_es_dueno_emp;

    @Column(name = "epe_es_reptec_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean epe_es_reptec_emp;

    @ManyToOne
    @JoinColumn(name = "epe_emp_id", referencedColumnName = "emp_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "epe_per_id", referencedColumnName = "per_id", nullable = false)
    private Persona persona;

}
