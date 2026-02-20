package com.meditrack.controller;

import com.meditrack.dto.alarmaconfig.AlarmaConfigRequestDto;
import com.meditrack.dto.alarmaconfig.AlarmaConfigResponseDto;
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

}
