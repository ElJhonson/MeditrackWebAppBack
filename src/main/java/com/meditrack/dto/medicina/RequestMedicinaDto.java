package com.meditrack.dto.medicina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMedicinaDto {

    private String nombre;
    private String dosageForm;
    private Date expirationDate;
    private Long pacienteId;

}
