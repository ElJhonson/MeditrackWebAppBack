package com.meditrack.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePacienteDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String cuidadorName;
    private List<String> cuidados;
    private Integer edad;
    private String curp;
    private List<String> enfermedadesCronicas;

}
