package com.meditrack.service;

import com.meditrack.dto.auth.AuthResponseDto;
import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.dto.paciente.ResponsePacientePerfilDto;
import com.meditrack.dto.paciente.UpdatePacientePerfilDto;
import com.meditrack.mapper.PacienteMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new IllegalStateException("El teléfono ya está registrado");
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
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Cuidador cuidador = cuidadorRepository.findByCodigoVinculacion(codigo)
                .orElseThrow(() -> new RuntimeException("Código de cuidador no válido"));

        if (paciente.getCuidador() != null && paciente.getCuidador().equals(cuidador)) {
            throw new RuntimeException("El paciente ya está vinculado a este cuidador");
        }
        paciente.setCuidador(cuidador);
        cuidador.getPacientes().add(paciente);

        cuidadorRepository.save(cuidador);
    }

    public ResponsePacienteDto obtenerMisDatos(String phoneNumberUsuarioActual) {
        User user = userRepo.findByPhoneNumber(phoneNumberUsuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado para este usuario"));

        return PacienteMapper.toResponse(paciente);
    }



    public void desvincularCuidador(String phoneNumber) {
        User user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
        Paciente paciente = user.getPaciente();
        if (paciente == null)
            throw new RuntimeException("El usuario no es un paciente");
        paciente.setCuidador(null);
        pacienteRepository.save(paciente);
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
    public ResponsePacientePerfilDto actualizarPerfilPaciente(
            Paciente paciente,
            UpdatePacientePerfilDto dto
    ) {

        PacienteMapper.updatePacienteFromDto(dto, paciente);

        return PacienteMapper.toResponsePerfil(paciente);
    }


}
