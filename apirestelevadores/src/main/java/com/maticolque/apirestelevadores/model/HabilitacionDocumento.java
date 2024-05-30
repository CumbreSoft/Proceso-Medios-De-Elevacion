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
@Table(name = "mde_habilitacion_docs")
public class HabilitacionDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hdo_id")
    private int hdo_id;

    @ManyToOne
    @JoinColumn(name = "hdo_eha_id", nullable = false)
    private EmpresaHabilitacion empresaHabilitacion;

    @ManyToOne
    @JoinColumn(name = "hdo_tad_id", nullable = false)
    private TipoAdjunto tipoAdjunto;

    @Column(name = "hdo_adjunto_orden", nullable = false)
    private int hdo_adjunto_orden;

    @Column(name = "hdo_adjunto_fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date hdo_adjunto_fecha;

    @ManyToOne
    @JoinColumn(name = "hdo_rev_id", nullable = false)
    private Revisor revisor;

}
