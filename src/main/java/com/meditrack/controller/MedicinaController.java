package com.meditrack.controller;

import com.meditrack.dto.medicina.RequestMedicinaDto;
import com.meditrack.dto.medicina.ResponseMedicinaDto;
import com.meditrack.service.MedicinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/medicinas")
@CrossOrigin("*")
public class MedicinaController {

    private final MedicinaService medicinaService;

    public MedicinaController(MedicinaService medicinaService) {
        this.medicinaService = medicinaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseMedicinaDto> registrar(
            @RequestBody RequestMedicinaDto dto,
            Principal principal) {

        ResponseMedicinaDto response = medicinaService.registrarMedicina(dto, principal.getName());
        return ResponseEntity.ok(response);
    }


    //Paciente ve sus medicinas
    @GetMapping("/mias")
    public ResponseEntity<List<ResponseMedicinaDto>> obtenerMisMedicinas(Principal principal) {
        return ResponseEntity.ok(medicinaService.obtenerMedicinasDelPaciente(principal.getName()));
    }

    //Cuidador ve las de un paciente vinculado
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ResponseMedicinaDto>> obtenerMedicinasPaciente(
            @PathVariable Long id,
            Principal principal) {
        return ResponseEntity.ok(medicinaService.obtenerMedicinasDePaciente(id, principal.getName()));
    }


}
