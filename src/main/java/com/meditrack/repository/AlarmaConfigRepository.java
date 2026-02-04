package com.meditrack.repository;

import com.meditrack.model.AlarmaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmaConfigRepository extends JpaRepository<AlarmaConfig, Long> {
    List<AlarmaConfig> findByPacienteIdAndActivoTrue(Long pacienteId);

}
