package com.meditrack.service;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.RequestPacienteByCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.mapper.CuidadorMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.Paciente;
import com.meditrack.model.Rol;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuidadorService {

    private final CuidadorRepository cuidadorRepository;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final PacienteRepository pacienteRepository;


    public CuidadorService(CuidadorRepository cuidadorRepository, UserRepository userRepo, PacienteRepository pacienteRepository) {
        this.cuidadorRepository = cuidadorRepository;
        this.userRepo = userRepo;
        this.pacienteRepository = pacienteRepository;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public ResponseCuidadorDto registrar(RequestCuidadorDto dto) {
        Optional<User> existente = userRepo.findByPhoneNumber(dto.getPhoneNumber());
        if (existente.isPresent()) {
            throw new IllegalStateException("El número de teléfono ya está registrado");
        }

        Cuidador cuidador = CuidadorMapper.toEntity(dto);

        User userGuardado = userRepo.save(cuidador.getUser());
        cuidador.setUser(userGuardado);

        Cuidador guardado = cuidadorRepository.save(cuidador);

        return CuidadorMapper.toResponse(guardado);
    }

    public List<ResponsePacienteDto> obtenerPacientesDeCuidador(String phoneNumberCuidador) {
        Cuidador cuidador = cuidadorRepository.findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        return cuidador.getPacientes().stream()
                .map(p -> new ResponsePacienteDto(
                        p.getId(),
                        p.getUser().getName(),
                        p.getUser().getPhoneNumber()
                ))
                .toList();
    }

    public ResponseCuidadorDto obtenerMisDatos(String phoneNumberCuidador) {
        Cuidador cuidador = cuidadorRepository.findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        return CuidadorMapper.toResponse(cuidador);
    }

    public ResponsePacienteDto registrarPacienteDesdeCuidador(
            String phoneNumberCuidador,
            RequestPacienteDto dto) {

        // 1. Obtener cuidador que está logueado
        Cuidador cuidador = cuidadorRepository.findByUserPhoneNumber(phoneNumberCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        // 2. Validar que el número no exista
        if (userRepo.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new IllegalStateException("El número ya está registrado");
        }

        // 3. Crear usuario (paciente)
        User user = new User();
        user.setName(dto.getNombre());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(Rol.PACIENTE);

        // 4. Crear paciente y VINCULAR AUTOMÁTICAMENTE
        Paciente paciente = new Paciente();
        paciente.setUser(user);
        paciente.setCuidador(cuidador);

        // 5. Guardar
        userRepo.save(user);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        return new ResponsePacienteDto(
                pacienteGuardado.getId(),
                user.getName(),
                user.getPhoneNumber()
        );
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

