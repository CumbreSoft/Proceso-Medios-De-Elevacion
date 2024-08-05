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
@Table(name = "mde_tipos_maquinas")
@EntityListeners(AuditingEntityListener.class)
public class TipoMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tma_id")
    private int tma_id;

    @Column(name = "tma_cod", nullable = false)
    private int tma_cod;

    @Column(name = "tma_detalle", length = 30)
    private String tma_detalle;

    @Column(name = "tma_activa", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tma_activa;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "tma_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "tma_fecha_modificacion")
    private LocalDateTime fecha_modificacion;
}
