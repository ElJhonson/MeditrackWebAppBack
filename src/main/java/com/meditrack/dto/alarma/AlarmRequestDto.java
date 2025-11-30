package com.meditrack.dto.alarma;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRequestDto {
    private Long pacienteId;
    private Long medicinaId;
    private String hora;
}
