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
@Table(name = "mde_distrito")
@EntityListeners(AuditingEntityListener.class)
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id")
    private int dis_id;

    @Column(name = "dis_codigo", nullable = false, length = 2)
    private String dis_codigo;

    @Column(name = "dis_nombre", nullable = false, length = 30)
    private String dis_nombre;

    @Column(name = "dis_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dis_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "dis_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "dis_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
