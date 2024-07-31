package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas_habilitacion")
@EntityListeners(AuditingEntityListener.class)
public class EmpresaHabilitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eha_id")
    private int eha_id;

    @Column(name = "eha_fecha", nullable = false)
    private LocalDate eha_fecha;

    @OneToOne
    @JoinColumn(name = "eha_emp_id", nullable = false)
    private Empresa empresa;

    @Column(name = "eha_expediente", nullable = false, length = 20)
    private String eha_expediente;

    @Column(name = "eha_habilitada", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean eha_habilitada;

    @Column(name = "eha_vto_hab", nullable = false)
    private LocalDate eha_vto_hab;

    @ManyToOne
    @JoinColumn(name = "eha_rev_id", nullable = false)
    private Revisor revisor;

    @Column(name = "eha_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean eha_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "eha_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "eha_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
