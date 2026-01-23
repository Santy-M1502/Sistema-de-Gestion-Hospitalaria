package com.SGH.hospital.controller;

import com.SGH.hospital.entity.Especialidad;
import com.SGH.hospital.service.EspecialidadService;
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

    // ============================================================
    // Crear Especialidad
    // ============================================================
    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad especialidad) {
        Especialidad creada = especialidadService.crear(especialidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // ============================================================
    // Listar / Obtener objetos
    // ============================================================
    @GetMapping
    public ResponseEntity<List<Especialidad>> listar() {
        return ResponseEntity.ok(especialidadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.obtener(id));
    }

    // ============================================================
    // Actualizad Datos
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizar(
            @PathVariable Long id,
            @RequestBody Especialidad especialidad) {
        return ResponseEntity.ok(especialidadService.actualizar(id, especialidad));
    }

    // ============================================================
    // Eliminar / Desactivar Datos
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
