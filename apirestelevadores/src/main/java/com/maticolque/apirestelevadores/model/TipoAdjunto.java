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
@Table(name = "mde_tipos_adjuntos")
@EntityListeners(AuditingEntityListener.class)
public class TipoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tad_id")
    private int tad_id;

    @Column(name = "tad_nombre", nullable = false, length = 30)
    private String tad_nombre;

    @Column(name = "tad_cod", nullable = false)
    private int tad_cod;

    @Column(name = "tad_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tad_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "tad_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "tad_fecha_modificacion")
    private LocalDateTime fecha_modificacion;
}
