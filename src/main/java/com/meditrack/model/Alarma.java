package com.meditrack.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alarmas")
public class Alarma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hora;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medicina_id")
    private Medicina medicina;
}
