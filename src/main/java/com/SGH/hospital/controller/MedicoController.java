package com.SGH.hospital.controller;

// ==================== IMPORTS ====================

// DTOs
import com.SGH.hospital.dto.medico.MedicoRequest;
import com.SGH.hospital.dto.medico.MedicoUpdateRequest;
import com.SGH.hospital.dto.medico.MedicoResponse;
import com.SGH.hospital.dto.horarioAtencion.HorarioAtencionDTO;

// Enums
import com.SGH.hospital.enums.EstadoUsuario;

// Service
import com.SGH.hospital.service.MedicoService;

// Validation
import jakarta.validation.Valid;

// Spring Data
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

// Spring Web
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Spring Security
import org.springframework.security.access.prepost.PreAuthorize;

// Spring Annotations
import org.springframework.web.bind.annotation.*;

// Java Collections
import java.util.Set;

// ==================== CONTROLLER ====================

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // ==================== CRUD Básico ====================

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

    // ==================== Especialidades ====================

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

    // ==================== Horarios ====================

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

    // ==================== Búsquedas y Filtros ====================

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

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<Page<MedicoResponse>> buscarConFiltros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Long especialidadId,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) EstadoUsuario estado,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<MedicoResponse> response = medicoService.buscarConFiltros(
                nombre, apellido, especialidadId, disponible, estado, pageable);
        return ResponseEntity.ok(response);
    }
}