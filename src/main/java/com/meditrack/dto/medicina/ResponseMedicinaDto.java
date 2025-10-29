package com.meditrack.dto.medicina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMedicinaDto {
    private Long id;
    private String nombre;
    private String dosageForm;
    private Date expirationDate;
    private String pacienteNombre;
    private String registradoPorNombre;
}
