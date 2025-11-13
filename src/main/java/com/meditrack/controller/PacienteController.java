package com.meditrack.controller;

import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.service.JWTService;
import com.meditrack.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pacientes")
@CrossOrigin("*")
public class PacienteController {

    private final PacienteService pacienteSrv;
    private final JWTService jwtService;

    public PacienteController(PacienteService pacienteSrv, JWTService jwtService) {
        this.pacienteSrv = pacienteSrv;
        this.jwtService = jwtService;
    }


    @PostMapping("/registro")
    public ResponseEntity<ResponsePacienteDto> registrar(@RequestBody RequestPacienteDto dto){
        ResponsePacienteDto response = pacienteSrv.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/vincular-cuidador")
    public ResponseEntity<Map<String, String>> vincularCuidador(
            @RequestParam String codigo,
            @RequestHeader("Authorization") String token) {

        String phoneNumber = jwtService.extractPhoneNumber(token.replace("Bearer ", ""));
        pacienteSrv.vincularCuidador(phoneNumber, codigo);

        return ResponseEntity.ok(Map.of("mensaje", "Paciente vinculado correctamente al cuidador"));
    }


    @GetMapping("/misdatos")
    public ResponseEntity<ResponsePacienteDto> obtenerMisDatos(Authentication authentication) {
        // Extrae el número de teléfono del token JWT (sub)
        String phoneNumber = authentication.getName();

        ResponsePacienteDto response = pacienteSrv.obtenerMisDatos(phoneNumber);
        return ResponseEntity.ok(response);
    }


}
