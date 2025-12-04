package com.meditrack.controller;

import com.meditrack.model.Contacto;
import com.meditrack.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping("/registrar/{pacienteId}")
    public ResponseEntity<Contacto> crearContacto(
            @PathVariable Long pacienteId,
            @RequestBody Contacto contacto
    ) {
        Contacto nuevo = contactoService.crearContacto(pacienteId, contacto);
        return ResponseEntity.ok(nuevo);
    }

}
