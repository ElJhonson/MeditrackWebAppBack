package com.meditrack.mapper;

import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigResponseDto;
import com.meditrack.model.AlarmaConfig;
import com.meditrack.model.Medicina;
import com.meditrack.model.Paciente;

import java.time.LocalDateTime;

public class AlarmaConfigMapper {

    private AlarmaConfigMapper() {}

    public static AlarmaConfig toEntity(
            AlarmaConfigRequestDto dto,
            Paciente paciente,
            Medicina medicina
    ) {
        AlarmaConfig config = new AlarmaConfig();
        config.setPaciente(paciente);
        config.setMedicina(medicina);
        config.setFechaInicio(dto.getFechaInicio());
        config.setFechaFin(dto.getFechaFin());
        config.setFrecuenciaHoras(dto.getFrecuenciaHoras());
        config.setActivo(true);
        config.setCreado(LocalDateTime.now());
        config.setActualizado(LocalDateTime.now());
        return config;
    }

    public static AlarmaConfigResponseDto toResponseDTO(AlarmaConfig entity) {
        AlarmaConfigResponseDto dto = new AlarmaConfigResponseDto();
        dto.setId(entity.getId());
        dto.setPacienteId(entity.getPaciente().getId());
        dto.setMedicinaId(entity.getMedicina().getId());
        dto.setMedicinaNombre(entity.getMedicina().getName());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        dto.setFrecuenciaHoras(entity.getFrecuenciaHoras());
        dto.setActivo(entity.isActivo());
        dto.setCreado(entity.getCreado());
        return dto;
    }
}
