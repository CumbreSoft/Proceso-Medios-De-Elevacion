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
@Table(name = "mde_destinos")
@EntityListeners(AuditingEntityListener.class)
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dst_id")
    private int dst_id;

    @Column(name = "dst_codigo", nullable = false, length = 3)
    private String dst_codigo;

    @Column(name = "dst_detalle", length = 40)
    private String dst_detalle;

    @Column(name = "dst_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dst_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "dst_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "dst_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
