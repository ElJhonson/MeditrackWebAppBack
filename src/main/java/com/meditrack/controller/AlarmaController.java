package com.meditrack.controller;

import com.meditrack.dto.alarma.AlarmRequestDto;
import com.meditrack.dto.alarma.AlarmResponseDto;
import com.meditrack.repository.PacienteRepository;
import com.meditrack.repository.UserRepository;
import com.meditrack.service.AlarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alarmas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AlarmaController {

    private final AlarmaService alarmaService;
    private final UserRepository userRepo;

    @PostMapping("/crear")
    public ResponseEntity<AlarmResponseDto> crear(@RequestBody AlarmRequestDto dto) {
        AlarmResponseDto respuesta = alarmaService.crearAlarma(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mias")
    public ResponseEntity<List<AlarmResponseDto>> verMisAlarmas(Authentication auth) {
        return ResponseEntity.ok(alarmaService.verMisAlarmas(auth));
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<AlarmResponseDto> editarHora(
            @PathVariable Long id,
            @RequestBody AlarmRequestDto dto,
            Authentication auth
    ) {
        AlarmResponseDto updated = alarmaService.editarAlarma(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Eliminar alarma
    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id, Authentication auth) {
        alarmaService.eliminarAlarma(id);
        return ResponseEntity.ok(Map.of("mensaje", "Alarma eliminada correctamente"));
    }
}
