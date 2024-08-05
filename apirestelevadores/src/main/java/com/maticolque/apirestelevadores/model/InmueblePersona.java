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
@Table(name = "mde_inmuebles_personas")
@EntityListeners(AuditingEntityListener.class)
public class InmueblePersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ipe_id")
    private int ipe_id;

    @Column(name = "ipe_es_admin_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ipe_es_admin_edif;

    @Column(name = "ipe_es_coprop_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ipe_es_coprop_edif;

    @ManyToOne
    @JoinColumn(name = "ipe_inm_id", nullable = false)
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "ipe_per_id", nullable = false)
    private Persona persona;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "ipe_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "ipe_fecha_modificacion")
    private LocalDateTime fecha_modificacion;



}
