package com.meditrack.dto.alarmaconfig;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AlarmaConfigRequestDto {

    private Long pacienteId;
    private Long medicinaId;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private int frecuenciaHoras;
}

