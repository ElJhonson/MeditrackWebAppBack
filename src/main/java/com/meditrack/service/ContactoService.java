package com.meditrack.service;

import com.meditrack.model.Contacto;
import com.meditrack.model.Paciente;
import com.meditrack.repository.ContactoRepository;
import com.meditrack.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final PacienteRepository pacienteRepository;
    private final ContactoRepository contactoRepository;

    public Contacto crearContacto(Long pacienteId, Contacto contacto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        contacto.setPaciente(paciente);
        return contactoRepository.save(contacto);
    }


}
