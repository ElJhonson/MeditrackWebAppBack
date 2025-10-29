package com.meditrack.controller;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.service.CuidadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuidadores")
public class CuidadorController {

    private final CuidadorService cuidadorSrv;

    public CuidadorController(CuidadorService cuidadorSrv) {
        this.cuidadorSrv = cuidadorSrv;
    }

    @PostMapping("/registro")
    public ResponseEntity<ResponseCuidadorDto> registrar(@RequestBody RequestCuidadorDto dto){
        ResponseCuidadorDto response = cuidadorSrv.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
