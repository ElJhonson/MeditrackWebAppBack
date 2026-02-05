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
    private final MedicinaRepository medicinaRepository;
    private final UserRepository userRepository;


    public AlarmaConfigService(
            AlarmaConfigRepository alarmaConfigRepository,
            AlarmaRepository alarmaRepository,
            MedicinaRepository medicinaRepository, UserRepository userRepository
    ) {
        this.alarmaConfigRepository = alarmaConfigRepository;
        this.alarmaRepository = alarmaRepository;
        this.medicinaRepository = medicinaRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea una configuración de alarma y genera automáticamente
     * las alarmas correspondientes.
     */
    @Transactional
    public AlarmaConfigResponseDto crear(
            AlarmaConfigRequestDto dto,
            String phoneNumber
    ) {

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRol() != Rol.PACIENTE) {
            throw new RuntimeException("Solo pacientes pueden crear alarmas");
        }

        Paciente paciente = user.getPaciente();

        Medicina medicina = medicinaRepository.findById(dto.getMedicinaId())
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        AlarmaConfig config = AlarmaConfigMapper.toEntity(dto, medicina);
        config.setPaciente(paciente);

        alarmaConfigRepository.save(config);

        List<Alarma> alarmas = generarAlarmas(config);

        alarmaRepository.saveAll(alarmas);

        return AlarmaConfigMapper.toResponseDTO(config);
    }


    /**
     * Genera las alarmas con base en la configuración.
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