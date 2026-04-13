package com.meditrack.dto.alarma;

import java.time.LocalDateTime;

import com.meditrack.model.EstadoAlarma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmaResponseDto {
    private Long id;
    private Long alarmaConfigId;
    private Long medicinaId;
    private String medicinaNombre;
    private String dosageForm;
    private LocalDateTime fechaHora;
    private EstadoAlarma estado;
    private boolean notificada;
}