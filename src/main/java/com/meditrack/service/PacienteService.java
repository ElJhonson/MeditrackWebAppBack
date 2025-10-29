package com.meditrack.service;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
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
        Optional<User> existente = userRepo.findByEmail(dto.getEmail());
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

    public void vincularCuidador(Long id, String codigo) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Cuidador cuidador = cuidadorRepository.findByCodigoVinculacion(codigo)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        paciente.setCuidador(cuidador);
        pacienteRepository.save(paciente);

    }

    public ResponsePacienteDto obtenerMisDatos(String emailUsuarioActual) {
        User user = userRepo.findByEmail(emailUsuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado para este usuario"));

        ResponsePacienteDto dto = PacienteMapper.toResponse(paciente);

        if (paciente.getCuidador() != null) {
            dto.setCuidados(
                    paciente.getCuidador().getPacientes().stream()
                            .map(p -> p.getUser().getName())
                            .toList()
            );
        }

        return dto;
    }

}
