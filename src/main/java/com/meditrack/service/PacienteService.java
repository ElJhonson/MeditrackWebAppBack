package com.meditrack.service;

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
    private final CuidadorRepository cuidadorRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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

    @Transactional
    public ResponsePacienteDto actualizarPerfil(Long id, UpdatePacientePerfilDto dto, String phoneNumberActual) {

        // Opcional: evitar que un paciente modifique a otro
        User user = userRepo.findByPhoneNumber(phoneNumberActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (paciente.getUser() == null || paciente.getUser().getId() != user.getId()) {
            throw new RuntimeException("No tienes permiso para modificar este perfil");
        }

        // Aplicar cambios
        PacienteMapper.updatePerfil(paciente, dto);

        Paciente actualizado = pacienteRepository.save(paciente);

        return PacienteMapper.toResponse(actualizado);
    }

    public ResponsePacientePerfilDto obtenerPerfil(String phoneNumberUsuarioActual) {
        User user = userRepo.findByPhoneNumber(phoneNumberUsuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado para este usuario"));

        return PacienteMapper.toPerfilResponse(paciente);
    }

    public ResponsePacientePerfilDto obtenerPerfilPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return PacienteMapper.toPerfilResponse(paciente);
    }


    @Transactional
    public ResponsePacientePerfilDto actualizarPerfil(Long id, UpdatePacientePerfilDto dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (dto.getNombre() != null)
            paciente.getUser().setName(dto.getNombre());

        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            paciente.getUser().setPassword(encoder.encode(dto.getPassword()));

        if (dto.getEdad() != null)
            paciente.setEdad(dto.getEdad());

        if (dto.getCurp() != null)
            paciente.setCurp(dto.getCurp());

        if (dto.getEnfermedadesCronicas() != null)
            paciente.setEnfermedadesCronicas(dto.getEnfermedadesCronicas());

        Paciente guardado = pacienteRepository.save(paciente);

        return PacienteMapper.toPerfilResponse(guardado);
    }

    public void desvincularCuidador(String phoneNumber) {

        // 1. Buscar usuario por su teléfono (del token)
        User user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener el paciente desde el User
        Paciente paciente = user.getPaciente();
        if (paciente == null)
            throw new RuntimeException("El usuario no es un paciente");

        // 3. Quitar cuidador
        paciente.setCuidador(null);

        // 4. Guardar paciente
        pacienteRepository.save(paciente);
    }


    public void cambiarCuidador(String phoneNumber, String codigoCuidador) {

        // 1. Usuario dueño del token
        User user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Paciente del usuario
        Paciente paciente = user.getPaciente();
        if (paciente == null)
            throw new RuntimeException("El usuario no es un paciente");

        // 3. Buscar nuevo cuidador por código
        Cuidador cuidador = cuidadorRepository.findByCodigoVinculacion(codigoCuidador)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        // 4. Asignar nuevo cuidador
        paciente.setCuidador(cuidador);

        // 5. Guardar
        pacienteRepository.save(paciente);
    }



}
