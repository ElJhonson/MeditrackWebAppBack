package com.meditrack.controller;

import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.service.JWTService;
import com.meditrack.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("pacientes/vincular")
    public ResponseEntity<String> vincularCuidador(
            @RequestParam String codigo,
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        String emailPaciente = jwtService.extractCorreo(jwt);
        pacienteSrv.vincularCuidadorPorCorreo(emailPaciente, codigo);

        return ResponseEntity.ok("Paciente vinculado correctamente al cuidador");
    }


    @GetMapping("/misdatos")
    public ResponseEntity<ResponsePacienteDto> obtenerMisDatos(@AuthenticationPrincipal String emailUsuarioActual) {
        return ResponseEntity.ok(pacienteSrv.obtenerMisDatos(emailUsuarioActual));
    }


}
