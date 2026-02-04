package com.meditrack.dto.alarma;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AlarmaResponseDto {

    private Long id;

    private Long alarmaConfigId;
    private Long medicinaId;
    private String medicinaNombre;

    private LocalDateTime fechaHora;

    private String estado;
    private boolean notificada;
}