package com.meditrack.dto.medicina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMedicinaDto {
    private Long id;
    private String nombre;
    private String dosageForm;
    private LocalDate expirationDate;
    private String pacienteNombre;
    private String registradoPorNombre;
}
