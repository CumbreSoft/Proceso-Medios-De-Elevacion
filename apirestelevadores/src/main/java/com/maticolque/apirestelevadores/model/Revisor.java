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
@Table(name = "mde_revisores")
@EntityListeners(AuditingEntityListener.class)
public class Revisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rev_id")
    private int rev_id;

    @Column(name = "rev_apellido",  length = 50)
    private String rev_apellido;

    @Column(name = "rev_nombre", nullable = false, length = 50)
    private String rev_nombre;

    @Column(name = "rev_cuit", length = 13)
    private String rev_cuit;

    @Column(name = "rev_tipodoc")
    private int rev_tipodoc;

    @Column(name = "rev_numdoc", nullable = false, length = 8)
    private String rev_numdoc;

    @Column(name = "rev_correo",  length = 40)
    private String rev_correo;

    @Column(name = "rev_telefono", length = 30)
    private String rev_telefono;

    @Column(name = "rev_usuario_sayges", length = 100)
    private String rev_usuario_sayges;

    @Column(name = "rev_aprob_mde", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_aprob_mde;

    @Column(name = "rev_renov_mde", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_renov_mde;

    @Column(name = "rev_aprob_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_aprob_emp;

    @Column(name = "rev_activo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rev_activo;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "rev_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "rev_fecha_modificacion")
    private LocalDateTime fecha_modificacion;
}
