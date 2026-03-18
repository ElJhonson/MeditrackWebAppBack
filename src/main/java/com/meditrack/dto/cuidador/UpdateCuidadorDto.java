package com.meditrack.dto.cuidador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCuidadorDto {
    private String name;
    private String phoneNumber;
    private String ocupacion;
}