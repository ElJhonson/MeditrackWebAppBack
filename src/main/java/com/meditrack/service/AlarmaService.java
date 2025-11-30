package com.meditrack.service;

import com.meditrack.dto.alarma.AlarmRequestDto;
import com.meditrack.dto.alarma.AlarmResponseDto;
import com.meditrack.model.Alarma;
import com.meditrack.model.Medicina;
import com.meditrack.model.Paciente;
import com.meditrack.repository.AlarmaRepository;
import com.meditrack.repository.MedicinaRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmaService {

    private final PacienteRepository pacienteRepo;
    private final MedicinaRepository medicinaRepo;
    private final AlarmaRepository alarmaRepo;
    private final UserRepository userRepo;

    public AlarmResponseDto crearAlarma(AlarmRequestDto dto) {

        Paciente paciente = pacienteRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medicina medicina = medicinaRepo.findById(dto.getMedicinaId())
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        Alarma alarma = new Alarma();
        alarma.setPaciente(paciente);
        alarma.setMedicina(medicina);
        alarma.setHora(dto.getHora());

        alarmaRepo.save(alarma);

        return new AlarmResponseDto(
                alarma.getId(),
                paciente.getId(),
                medicina.getId(),
                alarma.getMedicina().getName(),
                alarma.getHora()
        );
    }

    public List<AlarmResponseDto> verAlarmasDePaciente(Long pacienteId) {
        return alarmaRepo.findByPacienteId(pacienteId)
                .stream()
                .map(a -> new AlarmResponseDto(
                        a.getId(),
                        a.getPaciente().getId(),
                        a.getMedicina().getId(),
                        a.getMedicina().getName(),
                        a.getHora()
                ))
                .toList();
    }

    public List<AlarmResponseDto> verMisAlarmas(Authentication auth) {

        String phoneNumber = auth.getName();

        var user = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        Long pacienteId = user.getPaciente().getId();

        return verAlarmasDePaciente(pacienteId);
    }


}
