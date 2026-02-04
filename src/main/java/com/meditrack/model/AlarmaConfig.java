package com.meditrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alarma_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medicina_id", nullable = false)
    private Medicina medicina;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private int frecuenciaHoras;

    private boolean activo;

    private LocalDateTime creado;

    private LocalDateTime actualizado;

    @OneToMany(
            mappedBy = "alarmaConfig",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Alarma> alarmas = new ArrayList<>();

}

