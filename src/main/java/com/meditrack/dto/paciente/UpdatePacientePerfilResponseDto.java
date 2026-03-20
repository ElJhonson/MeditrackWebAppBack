package com.meditrack.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePacientePerfilResponseDto {
    private String message;
    private boolean requiresReauth;
    private ResponsePacientePerfilDto paciente;
}
