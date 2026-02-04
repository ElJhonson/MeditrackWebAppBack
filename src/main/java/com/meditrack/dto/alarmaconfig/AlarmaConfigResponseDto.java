package com.meditrack.dto.alarmaconfig;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AlarmaConfigResponseDto {

    private Long id;

    private Long pacienteId;
    private Long medicinaId;
    private String medicinaNombre;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private int frecuenciaHoras;
    private boolean activo;

    private LocalDateTime creado;
}
