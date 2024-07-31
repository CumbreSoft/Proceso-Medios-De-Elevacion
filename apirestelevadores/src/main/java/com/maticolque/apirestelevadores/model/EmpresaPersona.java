package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas_personas")
@EntityListeners(AuditingEntityListener.class)
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



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "epe_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "epe_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
