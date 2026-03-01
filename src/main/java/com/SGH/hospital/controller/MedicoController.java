package com.SGH.hospital.controller;

import com.SGH.hospital.dto.medico.MedicoRequest;
import com.SGH.hospital.dto.medico.MedicoUpdateRequest;
import com.SGH.hospital.entity.Consulta;
import com.SGH.hospital.dto.medico.MedicoResponse;
import com.SGH.hospital.dto.horarioAtencion.HorarioAtencionDTO;

import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.service.ConsultaService;
import com.SGH.hospital.service.MedicoService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

// ==================== CONTROLLER ====================

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;
    private final ConsultaService consultaService;

    public MedicoController(MedicoService medicoService, ConsultaService consultaService) {
        this.medicoService = medicoService;
        this.consultaService = consultaService;
    }

    // ============================================================
    // ==================== CRUD Básico ====================
    // ============================================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> crearMedico(@Valid @RequestBody MedicoRequest request) {
        MedicoResponse response = medicoService.crearMedico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMERO')")
    public ResponseEntity<MedicoResponse> obtenerPorId(@PathVariable Long id) {
        MedicoResponse response = medicoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<Page<MedicoResponse>> listarTodos(
            @PageableDefault(size = 10, sort = "apellido") Pageable pageable) {
        Page<MedicoResponse> response = medicoService.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<MedicoResponse> actualizarMedico(
            @PathVariable Long id,
            @Valid @RequestBody MedicoUpdateRequest request) {
        MedicoResponse response = medicoService.actualizarMedico(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoUsuario estado) {
        medicoService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Especialidades
    // ============================================================

    @PutMapping("/{id}/especialidades")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> asignarEspecialidades(
            @PathVariable Long id,
            @RequestBody Set<Long> especialidadIds) {
        MedicoResponse response = medicoService.asignarEspecialidades(id, especialidadIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/especialidades/{especialidadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> agregarEspecialidad(
            @PathVariable Long id,
            @PathVariable Long especialidadId) {
        MedicoResponse response = medicoService.agregarEspecialidad(id, especialidadId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/especialidades/{especialidadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> removerEspecialidad(
            @PathVariable Long id,
            @PathVariable Long especialidadId) {
        MedicoResponse response = medicoService.removerEspecialidad(id, especialidadId);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Horarios 
    // ============================================================

    @PutMapping("/{id}/horarios")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<MedicoResponse> configurarHorarios(
            @PathVariable Long id,
            @Valid @RequestBody Set<HorarioAtencionDTO> horarios) {
        MedicoResponse response = medicoService.configurarHorarios(id, horarios);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disponibilidad")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Void> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam Boolean disponible) {
        medicoService.cambiarDisponibilidad(id, disponible);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // Búsquedas y Filtros
    // ============================================================

    @GetMapping("/especialidad/{especialidadId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<Page<MedicoResponse>> buscarPorEspecialidad(
            @PathVariable Long especialidadId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<MedicoResponse> response = medicoService.buscarPorEspecialidad(especialidadId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<Page<MedicoResponse>> buscarDisponibles(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<MedicoResponse> response = medicoService.buscarDisponibles(pageable);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Lista Consultas
    // ============================================================

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<Consulta>> getConsultas(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.listarConsultasDeMedico(id));
    }
}