package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas")
@EntityListeners(AuditingEntityListener.class)
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



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "emp_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "emp_fecha_modificacion")
    private LocalDateTime fecha_modificacion;
}
