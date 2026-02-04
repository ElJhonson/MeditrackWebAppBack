package com.meditrack.mapper;


import com.meditrack.dto.alarma.AlarmaResponseDto;
import com.meditrack.model.Alarma;

public class AlarmaMapper {

    private AlarmaMapper() {}

    public static AlarmaResponseDto toResponseDTO(Alarma entity) {
        AlarmaResponseDto dto = new AlarmaResponseDto();
        dto.setId(entity.getId());
        dto.setAlarmaConfigId(entity.getAlarmaConfig().getId());
        dto.setMedicinaId(entity.getMedicina().getId());
        dto.setMedicinaNombre(entity.getMedicina().getName());
        dto.setFechaHora(entity.getFechaHora());
        dto.setEstado(entity.getEstado().name());
        dto.setNotificada(entity.isNotificada());
        return dto;
    }
}
