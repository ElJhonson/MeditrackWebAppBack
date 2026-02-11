package com.meditrack.service;

import com.meditrack.dto.auth.AuthResponseDto;
import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.mapper.CuidadorMapper;
import com.meditrack.mapper.PacienteMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CuidadorService {

    private final CuidadorRepository cuidadorRepository;
    private final UserRepository userRepo;
    private final PacienteRepository pacienteRepository;
    private final JWTService jwtService;


    public CuidadorService(CuidadorRepository cuidadorRepository, UserRepository userRepo, PacienteRepository pacienteRepository, JWTService jwtService) {
        this.cuidadorRepository = cuidadorRepository;
        this.userRepo = userRepo;
        this.pacienteRepository = pacienteRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponseDto registrar(RequestCuidadorDto dto) {

        Optional<User> existente = userRepo.findByPhoneNumber(dto.getPhoneNumber());
        if (existente.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El número ya está registrado"
            );
        }

        Cuidador cuidador = CuidadorMapper.toEntity(dto);

        User userGuardado = userRepo.save(cuidador.getUser());
        cuidador.setUser(userGuardado);

        cuidadorRepository.save(cuidador);

        String accessToken = jwtService.generateToken(userGuardado);
        String refreshToken = jwtService.generateRefreshToken(userGuardado);

        return new AuthResponseDto(accessToken, refreshToken);
    }


    public List<ResponsePacienteDto> obtenerPacientesDeCuidador(String phoneNumberCuidador) {
        Cuidador cuidador = cuidadorRepository.findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cuidador no encontrado"
        ));


        return cuidador.getPacientes().stream()
                .map(PacienteMapper::toResponse)
                .toList();

    }

    public ResponseCuidadorDto obtenerMisDatos(String phoneNumberCuidador) {
        Cuidador cuidador = cuidadorRepository.findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        return CuidadorMapper.toResponse(cuidador);
    }

    @Transactional
    public ResponsePacienteDto registrarPacienteDesdeCuidador(
            String phoneNumberCuidador,
            RequestPacienteDto dto) {

        // 1. Buscar cuidador autenticado
        Cuidador cuidador = cuidadorRepository
                .findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cuidador no encontrado"
                ));

        // 2. Validar teléfono único
        if (userRepo.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El número ya está registrado"
            );
        }

        // 3. Crear paciente (incluye User y vínculo con cuidador)
        Paciente paciente = PacienteMapper.toEntity(dto, cuidador);

        // 4. Guardar (cascade guarda User automáticamente)
        pacienteRepository.save(paciente);

        // 5. Respuesta
        return PacienteMapper.toResponse(paciente);
    }


    @Transactional
    public void desvincularPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Remueve la relación con el cuidador
        paciente.setCuidador(null);

        pacienteRepository.save(paciente);
    }

}

