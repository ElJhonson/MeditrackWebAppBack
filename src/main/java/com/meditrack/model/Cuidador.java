package com.meditrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuidadores")
public class Cuidador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relacionConPaciente;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(unique = true)
    private String codigoVinculacion;

    @OneToMany(mappedBy = "cuidador", cascade = CascadeType.ALL)
    private List<Paciente> pacientes = new ArrayList<>();

}
