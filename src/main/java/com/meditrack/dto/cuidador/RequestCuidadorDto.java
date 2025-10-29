package com.meditrack.dto.cuidador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCuidadorDto {
    private String name;
    private String email;
    private String password;

    private String relacionConPaciente;
}
