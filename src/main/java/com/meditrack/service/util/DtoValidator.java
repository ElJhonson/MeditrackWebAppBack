package com.meditrack.service.util;

import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import org.springframework.stereotype.Service;

@Service
public class DtoValidator {

    public void validarDto(AlarmaConfigRequestDto dto) {

        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            throw new RuntimeException("Fecha fin inválida");
        }

        if (dto.getFrecuenciaHoras() <= 0) {
            throw new RuntimeException("Frecuencia inválida");
        }

        if (dto.getFrecuenciaHoras() > 24) {
            throw new RuntimeException("Frecuencia demasiado alta");
        }
    }

}
