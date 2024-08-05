package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_medioselevacion")
@EntityListeners(AuditingEntityListener.class)
public class MedioElevacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mde_id")
    private int mde_id;

    @ManyToOne
    @JoinColumn(name = "mde_tma_id", nullable = false)
    private TipoMaquina tiposMaquinas;

    @Column(name = "mde_ubicacion", nullable = false, length = 200)
    private String mde_ubicacion;

    @Column(name = "mde_tipo", nullable = false, length = 25)
    private String mde_tipo;

    @Column(name = "mde_niveles", nullable = false)
    private int mde_niveles;

    @Column(name = "mde_planos_aprob", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mde_planos_aprob;

    @Column(name = "mde_expte_planos", nullable = false, length = 20)
    private String mde_expte_planos;

    @ManyToOne
    @JoinColumn(name = "mde_emp_id", nullable = true)
    private Empresa empresa;

    @Column(name = "mde_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mde_activo;


    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "mde_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "mde_fecha_modificacion")
    private LocalDateTime fecha_modificacion;
}
