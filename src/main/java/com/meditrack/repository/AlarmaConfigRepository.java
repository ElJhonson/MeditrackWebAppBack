package com.meditrack.repository;

import com.meditrack.model.AlarmaConfig;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlarmaConfigRepository extends JpaRepository<AlarmaConfig, Long> {
    @Query("""
    SELECT a FROM AlarmaConfig a
    WHERE a.paciente.id = :pacienteId
    AND a.activo = true
    AND (a.fechaFin IS NULL OR a.fechaFin > CURRENT_TIMESTAMP)
""")
    List<AlarmaConfig> findActivasVigentes(@Param("pacienteId") Long pacienteId);
    @Query("""
    SELECT a FROM AlarmaConfig a
    WHERE a.paciente.id = :pacienteId
    AND a.medicina.id = :medicinaId
    AND a.activo = true
    AND (a.fechaFin IS NULL OR a.fechaFin > CURRENT_TIMESTAMP)
""")
    List<AlarmaConfig> findActivasVigentesPorMedicina(
            Long pacienteId,
            Long medicinaId
    );    @Modifying
    @Transactional
    @Query("""
        UPDATE AlarmaConfig a
        SET a.activo = false,
            a.actualizado = :ahora
        WHERE a.activo = true
        AND a.fechaFin IS NOT NULL
        AND a.fechaFin <= :ahora
    """)
    int desactivarExpiradas(@Param("ahora") LocalDateTime ahora);

}
