package com.meditrack.mapper;


import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.Rol;
import com.meditrack.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PacienteMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public static Paciente toEntity(RequestPacienteDto dto, Cuidador cuidador) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getNombre());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(Rol.PACIENTE);

        Paciente paciente = new Paciente();
        paciente.setUser(user);
        paciente.setCuidador(cuidador);

        user.setPaciente(paciente);

        if (cuidador != null) {
            paciente.setCuidador(cuidador);
            cuidador.getPacientes().add(paciente);
        }

        user.setPaciente(paciente);

        return paciente;
    }

    public static ResponsePacienteDto toResponse(Paciente paciente) {
        if (paciente == null) return null;

        ResponsePacienteDto dto = new ResponsePacienteDto();
        dto.setId(paciente.getId());
        dto.setName(paciente.getUser().getName());
        dto.setPhoneNumber(paciente.getUser().getPhoneNumber());

        if (paciente.getCuidador() != null && paciente.getCuidador().getUser() != null) {
            dto.setCuidadorName(paciente.getCuidador().getUser().getName());
        } else {
            dto.setCuidadorName(null);
        }

        return dto;
    }

}
