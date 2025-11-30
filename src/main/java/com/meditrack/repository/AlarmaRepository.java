package com.meditrack.repository;

import com.meditrack.model.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmaRepository extends JpaRepository<Alarma, Long> {
    List<Alarma> findByPacienteId(Long pacienteId);

}
