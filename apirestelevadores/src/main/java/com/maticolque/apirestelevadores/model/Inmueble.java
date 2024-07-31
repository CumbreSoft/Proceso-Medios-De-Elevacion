package com.maticolque.apirestelevadores.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_inmuebles")
@EntityListeners(AuditingEntityListener.class)
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inm_id")
    private int inm_id;

    @Column(name = "inm_padron", nullable = false)
    private int inm_padron;

    @Column(name = "inm_direccion", nullable = false, length = 200)
    private String inm_direccion;

    @Column(name = "inm_cod_postal", nullable = false, length = 4)
    private String inm_cod_postal;

    @ManyToOne
    @JoinColumn(name = "inm_dis_id", nullable = false)
    private Distrito distrito;

    @ManyToOne
    @JoinColumn(name = "inm_dst_id", nullable = false)
    private Destino destino;

    @Column(name = "inm_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean inm_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "inm_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "inm_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

    /*@OneToMany(mappedBy = "inmueble")
    private Set<InmueblePersona> inmueblePersonas;*/

}
