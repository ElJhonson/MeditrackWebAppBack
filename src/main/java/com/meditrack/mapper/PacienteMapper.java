package com.meditrack.mapper;


import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.dto.paciente.ResponsePacientePerfilDto;
import com.meditrack.dto.paciente.UpdatePacientePerfilDto;
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
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(Rol.PACIENTE);

        Paciente paciente = new Paciente();
        paciente.setUser(user);
        paciente.setEdad(dto.getEdad()); // ✅ AQUÍ ESTABA EL ERROR
        paciente.setCuidador(cuidador);

        user.setPaciente(paciente);

        if (cuidador != null) {
            cuidador.getPacientes().add(paciente);
        }

        return paciente;
    }

    public static ResponsePacienteDto toResponse(Paciente paciente) {
        if (paciente == null) return null;

        ResponsePacienteDto dto = new ResponsePacienteDto();
        dto.setId(paciente.getId());
        dto.setName(paciente.getUser().getName());
        dto.setPhoneNumber(paciente.getUser().getPhoneNumber());
        dto.setEdad(paciente.getEdad());

        if (paciente.getCuidador() != null && paciente.getCuidador().getUser() != null) {
            dto.setCuidadorName(paciente.getCuidador().getUser().getName());
        } else {
            dto.setCuidadorName(null);
        }

        return dto;
    }

    public static void updatePerfil(Paciente paciente, UpdatePacientePerfilDto dto) {

        if (dto.getNombre() != null)
            paciente.getUser().setName(dto.getNombre());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            paciente.getUser().setPassword(encoder.encode(dto.getPassword()));
        }

        if (dto.getEdad() != null)
            paciente.setEdad(dto.getEdad());

        if (dto.getCurp() != null)
            paciente.setCurp(dto.getCurp());

        if (dto.getEnfermedadesCronicas() != null)
            paciente.setEnfermedadesCronicas(dto.getEnfermedadesCronicas());
    }

    public static ResponsePacientePerfilDto toPerfilResponse(Paciente paciente) {
        if (paciente == null) return null;

        ResponsePacientePerfilDto dto = new ResponsePacientePerfilDto();
        dto.setId(paciente.getId());
        if (paciente.getUser() != null) {
            dto.setName(paciente.getUser().getName());
            dto.setPhoneNumber(paciente.getUser().getPhoneNumber());
        }
        dto.setEdad(paciente.getEdad());
        dto.setCurp(paciente.getCurp());
        dto.setEnfermedadesCronicas(paciente.getEnfermedadesCronicas());
        return dto;
    }


}
