package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_empresas_habilitacion")
public class EmpresaHabilitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eha_id")
    private int eha_id;

    @Column(name = "eha_fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eha_fecha;

    @OneToOne
    @JoinColumn(name = "eha_emp_id", nullable = false)
    private Empresa empresa;

    @Column(name = "eha_expediente", nullable = false, length = 20)
    private String eha_expediente;

    @Column(name = "eha_habilitada", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean eha_habilitada;

    @Column(name = "eha_vto_hab", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eha_vto_hab;

    @OneToOne
    @JoinColumn(name = "eha_rev_id", nullable = false)
    private Revisor revisor;

}
