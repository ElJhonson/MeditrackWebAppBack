package com.meditrack.mapper;

import com.meditrack.dto.medicina.RequestMedicinaDto;
import com.meditrack.dto.medicina.ResponseMedicinaDto;
import com.meditrack.model.Medicina;
import com.meditrack.model.Paciente;
import com.meditrack.model.Rol;
import com.meditrack.model.User;

public class MedicinaMapper {

    public static Medicina toEntity(RequestMedicinaDto dto, Paciente paciente, User registradoPor) {
        if (dto == null) return null;

        Medicina medicina = new Medicina();
        medicina.setName(dto.getNombre());
        medicina.setDosageForm(dto.getDosageForm());
        medicina.setExpirationDate(dto.getExpirationDate());
        medicina.setPaciente(paciente);
        medicina.setRegistradoPor(registradoPor);

        return medicina;
    }

    public static ResponseMedicinaDto toResponse(Medicina medicina) {
        if (medicina == null) return null;

        String registradoPor;

        if (medicina.getRegistradoPor() != null &&
                medicina.getRegistradoPor().getRol() == Rol.CUIDADOR) {
            registradoPor = "Cuidador";
        } else {
            registradoPor = "Paciente";
        }

        ResponseMedicinaDto dto = new ResponseMedicinaDto();
        dto.setId(medicina.getId());
        dto.setNombre(medicina.getName());
        dto.setDosageForm(medicina.getDosageForm());
        dto.setExpirationDate(medicina.getExpirationDate());
        dto.setRegistradoPorNombre(registradoPor);


        if (medicina.getPaciente() != null &&
                medicina.getPaciente().getUser() != null) {
            dto.setPacienteNombre(
                    medicina.getPaciente().getUser().getName()
            );
        }

        return dto;
    }

}
