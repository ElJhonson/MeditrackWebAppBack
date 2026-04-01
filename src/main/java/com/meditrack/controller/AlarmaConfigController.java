package com.meditrack.controller;

import com.meditrack.dto.alarma.AlarmaResponseDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigResponseDto;
import com.meditrack.model.EstadoAlarma;
import com.meditrack.service.AlarmaConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
            Principal principal) {

        return ResponseEntity.ok(
                alarmaConfigService.obtenerPorPaciente(principal.getName())
        );
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<AlarmaResponseDto>> obtenerHoy(Principal principal) {
        return ResponseEntity.ok(
                alarmaConfigService.obtenerAlarmasDelDia(principal.getName())
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



}
