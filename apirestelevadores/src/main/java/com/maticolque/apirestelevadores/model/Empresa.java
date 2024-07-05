package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int emp_id;

    @Column(name = "emp_razon", nullable = false, length = 60)
    private String emp_razon;

    @Column(name = "emp_cuit", nullable = false, length = 13)
    private String emp_cuit;

    @Column(name = "emp_domic_legal", nullable = false, length = 200)
    private String emp_domic_legal;

    @Column(name = "emp_telefono", nullable = false, length = 30)
    private String emp_telefono;

    @Column(name = "emp_correo", nullable = false, length = 30)
    private String emp_correo;

    @Column(name = "emp_activa", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean emp_activa; //cambiar de emp_activa a activa para poder listar empresas con true o false
}
