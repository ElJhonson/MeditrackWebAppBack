package com.meditrack.repository;

import com.meditrack.model.Medicina;
import com.meditrack.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MedicinaRepository
        extends JpaRepository<Medicina, Long> {
    List<Medicina> findByPaciente(Paciente paciente);
}

