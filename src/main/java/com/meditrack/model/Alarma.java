package com.meditrack.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarmas")
@Data
public class Alarma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alarma_config_id", nullable = false)
    private AlarmaConfig alarmaConfig;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicina_id", nullable = false)
    private Medicina medicina;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private EstadoAlarma estado; // PENDIENTE, TOMADA, OMITIDA

    private boolean notificada;

    private LocalDateTime creado;
}

