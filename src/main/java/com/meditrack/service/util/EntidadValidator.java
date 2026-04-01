package com.meditrack.service.util;

import com.meditrack.model.*;
import com.meditrack.repository.AlarmaConfigRepository;
import com.meditrack.repository.MedicinaRepository;
import com.meditrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntidadValidator {

    private final UserRepository userRepository;
    private final MedicinaRepository medicinaRepository;
    private final AlarmaConfigRepository alarmaConfigRepository;

    public User usuario(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Paciente paciente(String phoneNumber) {
        User user = usuario(phoneNumber);

        if (user.getRol() != Rol.PACIENTE) {
            throw new RuntimeException("Solo pacientes pueden realizar esta acción");
        }
        return user.getPaciente();
    }
    public Medicina medicinaValida(Long medicinaId, Paciente paciente) {
        Medicina medicina = medicinaRepository.findById(medicinaId)
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        if (!medicina.getPaciente().getId().equals(paciente.getId())) {
            throw new RuntimeException("No autorizado");
        }

        return medicina;
    }

    public AlarmaConfig configValida(Long configId, Paciente paciente) {
        AlarmaConfig config = alarmaConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Config no encontrada"));

        if (!config.getPaciente().getId().equals(paciente.getId())) {
            throw new RuntimeException("No autorizado");
        }

        return config;
    }
}
