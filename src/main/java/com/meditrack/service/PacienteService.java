package com.meditrack.service;

import com.meditrack.dto.auth.AuthResponseDto;
import com.meditrack.dto.cuidador.CuidadorInfoDto;
import com.meditrack.dto.paciente.*;
import com.meditrack.mapper.PacienteMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final CuidadorRepository cuidadorRepository;

    public PacienteService(PacienteRepository pacienteRepository,
                           UserRepository userRepo, JWTService jwtService,
                           CuidadorRepository cuidadorRepository) {
        this.pacienteRepository = pacienteRepository;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.cuidadorRepository = cuidadorRepository;
    }

    @Transactional
    public AuthResponseDto registrar(RequestPacienteDto dto) {
        Optional<User> existente = userRepo.findByPhoneNumber(dto.getPhoneNumber());
        if (existente.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El teléfono ya está registrado"
            );
        }
        Cuidador cuidador = null;
        Paciente paciente = PacienteMapper.toEntity(dto, cuidador);
        Paciente guardado = pacienteRepository.save(paciente);

        User user = guardado.getUser();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponseDto(accessToken, refreshToken);
    }


    @Transactional
    public void vincularCuidador(String phoneNumberPaciente, String codigo) {
        Paciente paciente = userRepo.findByPhoneNumber(phoneNumberPaciente)
                .map(User::getPaciente)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paciente no encontrado"));
        Cuidador cuidador = cuidadorRepository.findByCodigoVinculacion(codigo)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Código de cuidador no válido"));
        if (paciente.getCuidador() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El paciente ya está vinculado a un cuidador");
        }
        paciente.setCuidador(cuidador);
        pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public CuidadorInfoDto buscarCuidadorPorCodigo(String codigo) {
        Cuidador cuidador = cuidadorRepository
                .findByCodigoVinculacion(codigo).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Código de cuidador no válido"));
        return new CuidadorInfoDto(
                cuidador.getUser().getName(),
                cuidador.getUser().getPhoneNumber());
    }

    public void desvincularCuidador(String phoneNumber) {
        User user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
        Paciente paciente = user.getPaciente();
        if (paciente == null)
            throw new RuntimeException("El usuario no es un paciente");
        if (paciente.getCuidador() == null)
            throw new RuntimeException("El paciente no tiene cuidador vinculado");
        paciente.setCuidador(null);
        pacienteRepository.save(paciente);
    }

    public ResponsePacienteDto obtenerPerfil(String phoneNumberUsuarioActual) {
        User user = userRepo.findByPhoneNumber(phoneNumberUsuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado para este usuario"));

        return PacienteMapper.toResponse(paciente);
    }


    public void cambiarCuidador(String phoneNumber, String codigoCuidador) {
        User user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
        Paciente paciente = user.getPaciente();
        if (paciente == null)
            throw new RuntimeException("El usuario no es un paciente");
        Cuidador cuidador = cuidadorRepository
                .findByCodigoVinculacion(codigoCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));
        paciente.setCuidador(cuidador);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public ResponsePacientePerfilDto actualizarPerfilPacienteDesdeCuidador(
            Paciente paciente,
            UpdatePacientePerfilDto dto
    ) {
        validarCambioTelefono(paciente, dto.getPhoneNumber());

        PacienteMapper.updatePacienteFromDto(dto, paciente);

        return PacienteMapper.toResponsePerfil(paciente);
    }

    @Transactional
    public UpdatePacientePerfilResponseDto actualizarPerfilPropio(
            String phoneNumber,
            UpdatePacientePerfilDto dto
    ) {

        Paciente paciente = pacienteRepository
                .findByUserPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        validarCambioTelefono(paciente, dto.getPhoneNumber());

        boolean requiresReauth = PacienteMapper.updatePacienteFromDto(dto, paciente);

        userRepo.save(paciente.getUser());

        return new UpdatePacientePerfilResponseDto(
                requiresReauth
                        ? "Teléfono actualizado. Se requiere iniciar sesión nuevamente."
                        : "Perfil actualizado correctamente",
                requiresReauth,
                PacienteMapper.toResponsePerfil(paciente)
        );
    }

    private void validarCambioTelefono(Paciente paciente, String nuevoTelefono) {

        if (nuevoTelefono == null || nuevoTelefono.isBlank()) return;

        String actual = paciente.getUser().getPhoneNumber();

        if (!nuevoTelefono.equals(actual)) {

            User existente = userRepo.findByPhoneNumber(nuevoTelefono).orElse(null);

            if (existente != null &&
                    !existente.getId().equals(paciente.getUser().getId())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El teléfono ya está registrado"
                );
            }
        }
    }

}
