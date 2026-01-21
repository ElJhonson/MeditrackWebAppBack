package com.meditrack.service;

import com.meditrack.model.Contacto;
import com.meditrack.model.Paciente;
import com.meditrack.repository.ContactoRepository;
import com.meditrack.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final PacienteRepository pacienteRepository;
    private final ContactoRepository contactoRepository;

    public Contacto crearContacto
            (Long pacienteId, Contacto contacto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new RuntimeException("Paciente no encontrado"));

        contacto.setPaciente(paciente);
        return contactoRepository.save(contacto);
    }

    public List<Contacto> getContactosByPaciente(Long pacienteId) {
        return contactoRepository.findByPacienteId(pacienteId);
    }

    public Contacto actualizarContacto(Long contactoId, Contacto datos) {
        Contacto contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() ->
                        new RuntimeException("Contacto no encontrado"));

        contacto.setNombre(datos.getNombre());
        contacto.setTelefono(datos.getTelefono());
        contacto.setParentesco(datos.getParentesco());

        return contactoRepository.save(contacto);
    }

    public void eliminarContacto(Long contactoId) {
        if (!contactoRepository.existsById(contactoId)) {
            throw new RuntimeException("Contacto no encontrado");
        }
        contactoRepository.deleteById(contactoId);
    }

}
