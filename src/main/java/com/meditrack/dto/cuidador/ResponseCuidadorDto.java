package com.meditrack.dto.cuidador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCuidadorDto {
    private Long id;
    private String name;
    private String email;
    private String relacionConPaciente;
    private List<String> pacientes;
}
