package com.meditrack.service;

import com.meditrack.dto.medicina.RequestMedicinaDto;
import com.meditrack.dto.medicina.ResponseMedicinaDto;
import com.meditrack.mapper.MedicinaMapper;
import com.meditrack.model.*;
import com.meditrack.repository.MedicinaRepository;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicinaService {

    private final MedicinaRepository medicinaRepository;
    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;

    public MedicinaService(MedicinaRepository medicinaRepository,
                           PacienteRepository pacienteRepository,
                           UserRepository userRepository) {
        this.medicinaRepository = medicinaRepository;
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
    }

    public ResponseMedicinaDto registrarMedicina(RequestMedicinaDto dto, String phoneNumber) {


        User registradoPor = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (registradoPor.getRol() == Rol.CUIDADOR) {
            Cuidador cuidador = registradoPor.getCuidador();
            boolean esPacienteDelCuidador = cuidador.getPacientes().stream()
                    .anyMatch(p -> p.getId().equals(paciente.getId()));
            if (!esPacienteDelCuidador) {
                throw new RuntimeException("No puedes registrar medicinas para un paciente no vinculado");
            }
        } else if (registradoPor.getRol() == Rol.PACIENTE) {
            if (!(paciente.getUser().getId() == registradoPor.getId())) {
                throw new RuntimeException("No puedes registrar medicinas para otro paciente");
            }
        }

        Medicina medicina = MedicinaMapper.toEntity(dto, paciente, registradoPor);
        Medicina guardada = medicinaRepository.save(medicina);

        return MedicinaMapper.toResponse(guardada);
    }

    public List<ResponseMedicinaDto> obtenerMedicinasDelPaciente(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRol() != Rol.PACIENTE) {
            throw new RuntimeException("Solo los pacientes pueden usar este método");
        }

        Paciente paciente = user.getPaciente();
        List<Medicina> medicinas = medicinaRepository.findByPaciente(paciente);

        return medicinas.stream()
                .map(MedicinaMapper::toResponse)
                .toList();
    }


    public List<ResponseMedicinaDto> obtenerMedicinasDePaciente(Long pacienteId, String phonNumberCuidador) {
        User user = userRepository.findByPhoneNumber(phonNumberCuidador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRol() != Rol.CUIDADOR) {
            throw new RuntimeException("Solo los cuidadores pueden ver medicinas de pacientes");
        }

        Cuidador cuidador = user.getCuidador();

        boolean esPacienteVinculado = cuidador.getPacientes().stream()
                .anyMatch(p -> p.getId().equals(pacienteId));

        if (!esPacienteVinculado) {
            throw new RuntimeException("No puedes ver medicinas de un paciente no vinculado");
        }

        Paciente paciente = cuidador.getPacientes().stream()
                .filter(p -> p.getId().equals(pacienteId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        List<Medicina> medicinas = medicinaRepository.findByPaciente(paciente);

        return medicinas.stream()
                .map(MedicinaMapper::toResponse)
                .toList();
    }

    public ResponseMedicinaDto obtenerPorId(Long id, String phoneNumber) {
        Medicina medicina = medicinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        validarAcceso(medicina.getPaciente(), phoneNumber);

        return MedicinaMapper.toResponse(medicina);
    }

    public ResponseMedicinaDto actualizarMedicina(Long id, RequestMedicinaDto dto, String phoneNumber) {
        Medicina medicina = medicinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        validarAcceso(medicina.getPaciente(), phoneNumber);

        medicina.setName(dto.getNombre());
        medicina.setDosageForm(dto.getDosageForm());
        medicina.setExpirationDate(dto.getExpirationDate());

        Medicina guardada = medicinaRepository.save(medicina);
        return MedicinaMapper.toResponse(guardada);
    }

    public void eliminarMedicina(Long id, String phoneNumber) {
        Medicina medicina = medicinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada"));

        validarAcceso(medicina.getPaciente(), phoneNumber);

        medicinaRepository.delete(medicina);
    }

    private void validarAcceso(Paciente paciente, User user) {
        if (user.getRol() == Rol.PACIENTE) {
            if (paciente.getUser().getId()!=(user.getId()))
                throw new RuntimeException("No puedes gestionar medicinas de otro paciente");
        }

        if (user.getRol() == Rol.CUIDADOR) {
            Cuidador cuidador = user.getCuidador();
            boolean vinculado = cuidador.getPacientes().stream()
                    .anyMatch(p -> p.getId().equals(paciente.getId()));

            if (!vinculado)
                throw new RuntimeException("Paciente no vinculado al cuidador");
        }
    }

    private void validarAcceso(Paciente paciente, String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarAcceso(paciente, user);
    }

}
