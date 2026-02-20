package com.meditrack.controller;

import com.meditrack.model.Contacto;
import com.meditrack.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contactos")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping("/registrar/{pacienteId}")
    public ResponseEntity<Contacto> crearContacto(
            @PathVariable Long pacienteId,
            @RequestBody Contacto contacto
    ) {
        Contacto nuevo = contactoService.
                crearContacto(pacienteId, contacto);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<List<Contacto>> obtenerContactos(
            @PathVariable Long pacienteId
    ) {
        return ResponseEntity.ok(contactoService.
                getContactosByPaciente(pacienteId));
    }

    @PutMapping("/actualizar/{contactoId}")
    public ResponseEntity<Contacto> actualizarContacto(
            @PathVariable Long contactoId,
            @RequestBody Contacto contactoActualizado
    ) {
        Contacto actualizado = contactoService.
                actualizarContacto(contactoId, contactoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{contactoId}")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Long contactoId) {
        contactoService.eliminarContacto(contactoId);
        return ResponseEntity.noContent().build();
    }

}
