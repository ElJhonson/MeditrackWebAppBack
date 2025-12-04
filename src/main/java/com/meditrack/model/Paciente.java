package com.meditrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "cuidador_id")
    private Cuidador cuidador;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicina> medicinas = new ArrayList<>();

    private Integer edad;

    private String curp;

    @ElementCollection
    @CollectionTable(name = "paciente_enfermedades", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "enfermedad")
    private List<String> enfermedadesCronicas = new ArrayList<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contacto> contactos = new ArrayList<>();


}
