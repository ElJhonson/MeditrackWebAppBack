package com.meditrack.scheduler;

import com.meditrack.repository.AlarmaConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmaScheduler {

    private final AlarmaConfigRepository repository;

    @Scheduled(fixedRate = 60000) // cada 1 minuto
    @Transactional
    public void desactivarAlarmasExpiradas() {

        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Mexico_City"));

        int actualizadas = repository.desactivarExpiradas(ahora);

        if (actualizadas > 0) {
            log.info("Alarmas desactivadas automáticamente: {}", actualizadas);
        }
    }
}