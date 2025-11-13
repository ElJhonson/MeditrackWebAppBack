package com.meditrack.controller;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.dto.paciente.ResponsePacienteDto;
import com.meditrack.model.User;
import com.meditrack.service.CuidadorService;
import com.meditrack.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuidadores")
@CrossOrigin("*")
public class CuidadorController {

    private final CuidadorService cuidadorSrv;
    private final JWTService jwtService;

    public CuidadorController(CuidadorService cuidadorSrv, JWTService jwtService) {
        this.cuidadorSrv = cuidadorSrv;
        this.jwtService = jwtService;
    }

    @PostMapping("/registro")
    public ResponseEntity<ResponseCuidadorDto> registrar(@RequestBody RequestCuidadorDto dto){
        ResponseCuidadorDto response = cuidadorSrv.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pacientes-del-cuidador")
    public ResponseEntity<List<ResponsePacienteDto>> obtenerPacientesDelCuidador(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        String phoneNumber = jwtService.extractPhoneNumber(jwt);
        List<ResponsePacienteDto> pacientes = cuidadorSrv.obtenerPacientesDeCuidador(phoneNumber);

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

}
