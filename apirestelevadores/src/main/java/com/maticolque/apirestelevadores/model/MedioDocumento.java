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
@Table(name = "mde_medios_docs")
public class MedioDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mdo_id")
    private int mdo_id;

    @ManyToOne
    @JoinColumn(name = "mdo_mha_id", nullable = false)
    private MedioHabilitacion medioHabilitacion;

    @ManyToOne
    @JoinColumn(name = "mdo_tad_id", nullable = false)
    private TipoAdjunto tipoAdjunto;

    @Column(name = "mdo_adjunto_orden", nullable = false)
    private int mdo_adjunto_orden;

    @Column(name = "mdo_adjunto_fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date mdo_adjunto_fecha;

    @ManyToOne
    @JoinColumn(name = "mdo_rev_id", nullable = false)
    private Revisor revisor;

}
