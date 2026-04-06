package com.meditrack.repository;

import com.meditrack.model.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlarmaRepository extends JpaRepository<Alarma, Long> {
    @Query("""
    SELECT a FROM Alarma a
    JOIN FETCH a.medicina m
    JOIN FETCH a.alarmaConfig c
    WHERE a.paciente.id = :pacienteId
    AND c.activo = true
    AND (c.fechaFin IS NULL OR c.fechaFin > CURRENT_TIMESTAMP)
    AND a.fechaHora BETWEEN :inicio AND :fin
    ORDER BY a.fechaHora ASC
""")
    List<Alarma> findAlarmasDelDia(
            @Param("pacienteId") Long pacienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
    void deleteByAlarmaConfigIdAndFechaHoraGreaterThanEqual(Long configId, LocalDateTime fecha);
}
