package com.meditrack.controller;

import com.meditrack.dto.auth.AuthResponseDto;
import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.dto.paciente.RequestPacienteDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.dto.paciente.ResponsePacientePerfilDto;
import com.meditrack.dto.paciente.UpdatePacientePerfilDto;
import com.meditrack.service.CuidadorService;
import com.meditrack.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuidadores")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CuidadorController {

    private final CuidadorService cuidadorSrv;
    private final JWTService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDto> registrar(
            @RequestBody RequestCuidadorDto dto
    ) {
        AuthResponseDto response = cuidadorSrv.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/pacientes-del-cuidador")
    public ResponseEntity<List<ResponsePacienteDto>> obtenerPacientesDelCuidador(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);
        List<ResponsePacienteDto> pacientes =
                cuidadorSrv.obtenerPacientesDeCuidador(phoneNumber);

        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/mis-datos")
    public ResponseEntity<ResponseCuidadorDto> obtenerMisDatosCuidador(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);

        ResponseCuidadorDto dto = cuidadorSrv.obtenerMisDatos(phoneNumber);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/registrar-paciente")
    public ResponseEntity<ResponsePacienteDto> registrarPacienteDesdeCuidador(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestPacienteDto dto
    ) {
        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);

        ResponsePacienteDto paciente =
                cuidadorSrv.registrarPacienteDesdeCuidador(phoneNumber, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @DeleteMapping("/{id}/desvincular")
    public ResponseEntity<Void> desvincularPaciente(@PathVariable Long id) {
        cuidadorSrv.desvincularPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pacientes/{pacienteId}")
    public ResponseEntity<ResponsePacientePerfilDto> actualizarPaciente(
            @RequestHeader("Authorization") String token,
            @PathVariable Long pacienteId,
            @RequestBody UpdatePacientePerfilDto dto
    ) {

        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);

        ResponsePacientePerfilDto response =
                cuidadorSrv.actualizarPacienteDesdeCuidador(
                        pacienteId,
                        phoneNumber,
                        dto
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<ResponsePacientePerfilDto> obtenerPaciente(
            @RequestHeader("Authorization") String token,
            @PathVariable Long pacienteId
    ) {

        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);

        ResponsePacientePerfilDto response =
                cuidadorSrv.obtenerPacienteDesdeCuidador(
                        pacienteId,
                        phoneNumber
                );

        return ResponseEntity.ok(response);
    }

}
