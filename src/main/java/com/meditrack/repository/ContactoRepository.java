package com.meditrack.repository;

import com.meditrack.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepository
        extends JpaRepository<Contacto, Long> {
    List<Contacto> findByPacienteId(Long pacienteId);

}
