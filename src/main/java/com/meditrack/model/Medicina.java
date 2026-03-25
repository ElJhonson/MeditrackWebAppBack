package com.meditrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicinas")
public class Medicina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String name;
    @Column(name = "forma_farmaceutica")
    private String dosageForm;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    @Column(nullable = false, name = "fecha_expiracion")
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "registrado_por_id")
    private User registradoPor;

    @OneToMany(mappedBy = "medicina")
    @JsonIgnore
    private List<AlarmaConfig> alarmasConfig = new ArrayList<>();

}
