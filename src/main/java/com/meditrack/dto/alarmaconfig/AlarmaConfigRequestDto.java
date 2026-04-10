package com.meditrack.dto.alarmaconfig;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AlarmaConfigRequestDto {

    private Long medicinaId;
    private Long pacienteId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private int frecuenciaHoras;
}


