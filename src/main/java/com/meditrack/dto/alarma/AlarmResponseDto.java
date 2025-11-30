package com.meditrack.dto.alarma;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponseDto {
    private Long id;
    private Long pacienteId;
    private Long medicinaId;
    private String medicinaNombre;
    private String hora;

}
