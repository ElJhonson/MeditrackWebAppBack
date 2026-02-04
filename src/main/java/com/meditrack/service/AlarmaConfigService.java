package com.meditrack.service;

import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigResponseDto;
import org.springframework.stereotype.Service;
import com.meditrack.mapper.AlarmaConfigMapper;
import com.meditrack.model.*;
import com.meditrack.repository.*;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmaConfigService {

    private final AlarmaConfigRepository alarmaConfigRepository;
    private final AlarmaRepository alarmaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicinaRepository medicinaRepository;
    private final UserRepository userRepository;


    public AlarmaConfigService(
            AlarmaConfigRepository alarmaConfigRepository,
            AlarmaRepository alarmaRepository,
            PacienteRepository pacienteRepository,
            MedicinaRepository medicinaRepository, UserRepository userRepository
    ) {
        this.alarmaConfigRepository = alarmaConfigRepository;
        this.alarmaRepository = alarmaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicinaRepository = medicinaRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea una configuración de alarma y genera automáticamente
     * las alarmas correspondientes.
     */
    @Transactional
    public AlarmaConfigResponseDto crear(AlarmaConfigRequestDto dto) {

        // 1️⃣ Obtener entidades base
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medicina medicina = medicinaRepository.findById(dto.getMedicinaId())
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        // 2️⃣ Validaciones básicas
        if (dto.getFechaInicio().isAfter(dto.getFechaFin())) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        if (dto.getFrecuenciaHoras() <= 0) {
            throw new RuntimeException("La frecuencia debe ser mayor a 0 horas");
        }

        // 3️⃣ Crear y guardar configuración
        AlarmaConfig config = AlarmaConfigMapper.toEntity(dto, paciente, medicina);
        AlarmaConfig configGuardada = alarmaConfigRepository.save(config);

        // 4️⃣ Generar alarmas
        List<Alarma> alarmas = generarAlarmas(configGuardada);

        alarmaRepository.saveAll(alarmas);

        // 5️⃣ Retornar respuesta
        return AlarmaConfigMapper.toResponseDTO(configGuardada);
    }

    /**
     * Genera las alarmas en base a la configuración.
     */
    private List<Alarma> generarAlarmas(AlarmaConfig config) {
        List<Alarma> alarmas = new ArrayList<>();

        LocalDateTime fecha = config.getFechaInicio();

        while (!fecha.isAfter(config.getFechaFin())) {

            Alarma alarma = new Alarma();
            alarma.setAlarmaConfig(config);
            alarma.setPaciente(config.getPaciente());
            alarma.setMedicina(config.getMedicina());
            alarma.setFechaHora(fecha);
            alarma.setEstado(EstadoAlarma.PENDIENTE);
            alarma.setNotificada(false);
            alarma.setCreado(LocalDateTime.now());

            alarmas.add(alarma);

            fecha = fecha.plusHours(config.getFrecuenciaHoras());
        }

        return alarmas;
    }

    /**
     * Obtiene todas las configuraciones activas de un paciente.
     */
    public List<AlarmaConfigResponseDto> obtenerPorPaciente(String phoneNumber) {

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRol() != Rol.PACIENTE) {
            throw new RuntimeException("Solo los pacientes pueden ver sus alarmas");
        }

        Paciente paciente = user.getPaciente();

        List<AlarmaConfig> alarmas =
                alarmaConfigRepository.findByPacienteIdAndActivoTrue(paciente.getId());

        return alarmas.stream()
                .map(AlarmaConfigMapper::toResponseDTO)
                .toList();
    }

}