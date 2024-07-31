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
@Table(name = "mde_inmuebles_medioselevacion")
@EntityListeners(AuditingEntityListener.class)
public class InmuebleMedioElevacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ime_id")
    private int ime_id;

    @ManyToOne
    @JoinColumn(name = "ime_inm_id", referencedColumnName = "inm_id", nullable = false)
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "ime_mde_id", referencedColumnName = "mde_id", nullable = false)
    private MedioElevacion medioElevacion;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "ime_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "ime_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

}
