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
@Table(name = "mde_medios_docs")
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDate mdo_adjunto_fecha;

    @ManyToOne
    @JoinColumn(name = "mdo_rev_id", nullable = false)
    private Revisor revisor;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "mdo_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "mdo_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
