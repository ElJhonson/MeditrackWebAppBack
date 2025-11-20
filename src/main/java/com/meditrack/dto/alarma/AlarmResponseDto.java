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
    private String medicina;
    private String hora;
    private Boolean activa;
    private String fechaCreacion;
}
