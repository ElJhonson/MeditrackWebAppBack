package com.meditrack.controller;

import com.meditrack.dto.alarma.AlarmaResponseDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigResponseDto;
import com.meditrack.model.EstadoAlarma;
import com.meditrack.service.AlarmaConfigService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/alarmas")
public class AlarmaConfigController {

    private final AlarmaConfigService alarmaConfigService;

    public AlarmaConfigController(AlarmaConfigService alarmaConfigService) {
        this.alarmaConfigService = alarmaConfigService;
    }

    @PostMapping("/crear")
    public ResponseEntity<AlarmaConfigResponseDto> crearConfiguracionAlarma(
            @RequestBody AlarmaConfigRequestDto alarmaConfig,
            Principal principal
    ) {
        AlarmaConfigResponseDto alarma =
                alarmaConfigService.crear(alarmaConfig, principal.getName());

        return ResponseEntity.ok(alarma);
    }


    @GetMapping("/mias")
    public ResponseEntity<List<AlarmaConfigResponseDto>> obtenerAlarmasConfig(
            @RequestParam(required = false) Long pacienteId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                alarmaConfigService.obtenerPorPaciente(
                        principal.getName(),
                        pacienteId
                )
        );
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<AlarmaResponseDto>> obtenerHoy(
            @RequestParam(required = false) Long pacienteId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                alarmaConfigService.obtenerAlarmasDelDia(
                        principal.getName(),
                        pacienteId
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlarmaConfigResponseDto> actualizar(
            @PathVariable Long id,
            @RequestBody AlarmaConfigRequestDto dto,
            Principal principal
    ) {
        return ResponseEntity.ok(
                alarmaConfigService.actualizar(id, dto, principal.getName())
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoAlarma estado,
            Principal principal
    ) {
        alarmaConfigService.actualizarEstado(id, estado, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Principal principal
    ) {
        alarmaConfigService.eliminar(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicina/{medicinaId}")
    public ResponseEntity<List<AlarmaConfigResponseDto>> obtenerPorMedicina(
            @PathVariable Long medicinaId,
            @RequestParam(required = false) Long pacienteId,
            Principal principal
    ) {
        return ResponseEntity.ok(
                alarmaConfigService.obtenerPorMedicinaId(
                        medicinaId,
                        principal.getName(),
                        pacienteId
                )
        );
    }

    @GetMapping("/historial")
    public ResponseEntity<List<AlarmaResponseDto>> obtenerHistorial(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Principal principal
    ) {
        return ResponseEntity.ok(
                alarmaConfigService.obtenerHistorial(
                        principal.getName(),
                        pacienteId,
                        fechaInicio,
                        fechaFin
                )
        );
    }
}
