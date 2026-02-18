package com.meditrack.dto.paciente;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePacientePerfilDto {

    private String nombre;
    private Integer edad;
    private String curp;
    private List<String> enfermedadesCronicas;

}
