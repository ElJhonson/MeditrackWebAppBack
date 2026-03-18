package com.meditrack.dto.cuidador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCuidadorResponseDto {
    private String message;
    private boolean requiresReauth;
    private ResponseCuidadorDto cuidador;
}