package com.maticolque.apirestelevadores.model;
import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "mde_personas", schema = "medios_de_elevacion")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    private int per_id;

    @Column(name = "per_nombre", nullable = false, length = 50)
    private String per_nombre;

    @Column(name = "per_tipodoc", nullable = false)
    private int per_tipodoc;

    @Column(name = "per_numdoc", nullable = false, length = 8)
    private String per_numdoc;

    @Column(name = "per_telefono", nullable = false, length = 12)
    private String per_telefono;

    @Column(name = "per_correo", nullable = false, length = 40)
    private String per_correo;

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
}
