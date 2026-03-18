package com.meditrack.mapper;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.dto.cuidador.UpdateCuidadorDto;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Rol;
import com.meditrack.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;
import java.util.stream.Collectors;

public class CuidadorMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public static Cuidador toEntity(RequestCuidadorDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(Rol.CUIDADOR);

        Cuidador cuidador = new Cuidador();
        cuidador.setOcupacion(dto.getOcupacion());
        cuidador.setCodigoVinculacion(generarCodigo());
        cuidador.setUser(user);

        user.setCuidador(cuidador);

        return cuidador;
    }

    public static ResponseCuidadorDto toResponse(Cuidador cuidador) {
        if (cuidador == null) return null;

        ResponseCuidadorDto dto = new ResponseCuidadorDto();
        dto.setId(cuidador.getId());
        dto.setName(cuidador.getUser().getName());
        dto.setPhoneNumber(cuidador.getUser().getPhoneNumber());
        dto.setOcupacion(cuidador.getOcupacion());
        dto.setCodigoVinculacion(cuidador.getCodigoVinculacion());

        if (cuidador.getPacientes() != null) {
            dto.setPacientes(
                    cuidador.getPacientes()
                            .stream()
                            .map(p -> p.getUser().getName())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static boolean updateEntity(Cuidador cuidador, UpdateCuidadorDto dto) {
        if (cuidador == null || dto == null) return false;

        boolean requiresReauth = false;

        User user = cuidador.getUser();

        if (dto.getPhoneNumber() != null &&
                !dto.getPhoneNumber().equals(user.getPhoneNumber())) {

            user.setPhoneNumber(dto.getPhoneNumber());
            requiresReauth = true;
        }

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getOcupacion() != null) {
            cuidador.setOcupacion(dto.getOcupacion());
        }

        return requiresReauth;
    }

    private static String generarCodigo() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

}
