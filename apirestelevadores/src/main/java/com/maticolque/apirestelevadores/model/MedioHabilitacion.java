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
@Table(name = "mde_medios_habilitacion")
public class MedioHabilitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mha_id")
    private int mha_id;

    @Column(name = "mha_inm_padron_guardado", nullable = false, length = 20)
    private int mha_inm_padron_guardado;

    @ManyToOne
    @JoinColumn(name = "mha_mde_id", nullable = false)
    private MedioElevacion medioElevacion;

    @ManyToOne
    @JoinColumn(name = "mha_emp_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "mha_per_id", nullable = false)
    private Persona persona;

    @Column(name = "mha_fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mha_fecha;

    @Column(name = "mha_expediente", nullable = false, length = 20)
    private String mha_expediente;

    @Column(name = "mha_fecha_vto", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mha_fecha_vto;

    @Column(name = "mha_fecha_pago", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mha_fecha_pago;

    @Column(name = "mha_fecha_inspec", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mha_fecha_inspec;

    /*@Column(name = "mha_planos_aprob", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mha_planos_aprob;*/

    @Column(name = "mha_habilitado", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mha_habilitado;

    @Column(name = "mha_oblea_entregada", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mha_oblea_entregada;

    @Column(name = "mha_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean mha_activo;

    @ManyToOne
    @JoinColumn(name = "mha_rev_id", nullable = false)
    private Revisor revisor;

    /*@Column(name = "mha_vto_hab", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mha_vto_hab;*/
}
