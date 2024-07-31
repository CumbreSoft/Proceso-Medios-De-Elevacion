package com.maticolque.apirestelevadores.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_personas")
@EntityListeners(AuditingEntityListener.class)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    private int per_id;

    @Column(name = "per_nombre",length = 50)
    private String per_nombre;

    @Column(name = "per_apellido",  length = 50)
    private String per_apellido;

    @Column(name = "per_cuit", length = 13)
    private String per_cuit;

    @Column(name = "per_tipodoc")
    private int per_tipodoc;

    @Column(name = "per_numdoc", length = 8)
    private String per_numdoc;

    @Column(name = "per_telefono", length = 30)
    private String per_telefono;

    @Column(name = "per_correo",  length = 40)
    private String per_correo;

    @Column(name = "per_domic_legal", length = 50)
    private String per_domic_legal;

    @Column(name = "per_es_dueno_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean per_es_dueno_emp;

    @Column(name = "per_es_reptec_emp", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean per_es_reptec_emp;

    @Column(name = "per_es_admin_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean per_es_admin_edif;

    @Column(name = "per_es_coprop_edif", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean per_es_coprop_edif;

    @Column(name = "per_activa", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean per_activa;



    //Guardar fechas de creacion y modificacion
    @CreatedDate
    @Column(name = "per_fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "per_fecha_modificacion")
    private LocalDateTime fecha_modificacion;

    /*@OneToMany(mappedBy = "persona")
    private Set<EmpresaPersona> empresaPersonas;

    @OneToMany(mappedBy = "persona")
    private Set<InmueblePersona> inmueblePersonas;*/
}
