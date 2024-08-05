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
@Table(name = "mde_habilitacion_docs")
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDate hdo_adjunto_fecha;

    @ManyToOne
    @JoinColumn(name = "hdo_rev_id", nullable = false)
    private Revisor revisor;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "hdo_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "hdo_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
