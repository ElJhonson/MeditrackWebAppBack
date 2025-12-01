package com.meditrack.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePacientePerfilDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private Integer edad;
    private String curp;
    private List<String> enfermedadesCronicas;
}
