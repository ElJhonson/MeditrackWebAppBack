package com.meditrack.dto.cuidador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPacienteByCuidadorDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private Integer edad;
    private String curp;
    private List<String> enfermedadesCronicas;
}
