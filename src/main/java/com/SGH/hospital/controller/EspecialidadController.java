package com.SGH.hospital.controller;

import com.SGH.hospital.dto.especialidad.EspecialidadRequest;
import com.SGH.hospital.dto.especialidad.EspecialidadResponse;
import com.SGH.hospital.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponse> crear(@Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponse>> listar() {
        return ResponseEntity.ok(especialidadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.ok(especialidadService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}