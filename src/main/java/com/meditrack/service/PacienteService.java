package com.meditrack.service;

import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.mapper.PacienteMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepo;
    private final CuidadorRepository cuidadorRepository;

    public PacienteService(PacienteRepository pacienteRepository,
                           UserRepository userRepo,
                           CuidadorRepository cuidadorRepository) {
        this.pacienteRepository = pacienteRepository;
        this.userRepo = userRepo;
        this.cuidadorRepository = cuidadorRepository;
    }

    public ResponsePacienteDto registrar(RequestPacienteDto dto) {
        Optional<User> existente = userRepo.findByPhoneNumber(dto.getPhoneNumber());
        if (existente.isPresent()) {
            throw new IllegalStateException("El correo ya está registrado");
        }

        Cuidador cuidador = null;
        if (dto.getCuidadorId() != null) {
            cuidador = cuidadorRepository.findById(dto.getCuidadorId())
                    .orElseThrow(() -> new IllegalStateException("Cuidador no encontrado"));
        }

        Paciente paciente = PacienteMapper.toEntity(dto, cuidador);
        Paciente guardado = pacienteRepository.save(paciente);

        System.out.println("Nombre: " + guardado.getUser().getName());
        return PacienteMapper.toResponse(guardado);
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



}
