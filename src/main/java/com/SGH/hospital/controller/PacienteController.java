package com.SGH.hospital.controller;

import com.SGH.hospital.dto.paciente.PacienteRequest;
import com.SGH.hospital.dto.paciente.PacienteResponse;
import com.SGH.hospital.dto.paciente.PacienteUpdateRequest;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * POST /api/pacientes - Crear un nuevo paciente
     * Accesible para ADMIN y el propio registro público
     */
    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.crearPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/pacientes/{id} - Obtener paciente por ID
     * Accesible para ADMIN, MEDICO y el propio PACIENTE
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<PacienteResponse> obtenerPorId(@PathVariable Long id) {
        PacienteResponse response = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/dni/{dni} - Obtener paciente por DNI
     * Accesible para ADMIN y MEDICO
     */
    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<PacienteResponse> obtenerPorDni(@PathVariable String dni) {
        PacienteResponse response = pacienteService.obtenerPorDni(dni);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/email/{email} - Obtener paciente por email
     * Accesible para ADMIN
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteResponse> obtenerPorEmail(@PathVariable String email) {
        PacienteResponse response = pacienteService.obtenerPorEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes - Listar todos los pacientes con paginación
     * Parámetros: page (default 0), size (default 10), sort (default createdAt,desc)
     * Accesible para ADMIN y MEDICO
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<PacienteResponse>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PacienteResponse> response = pacienteService.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/activos - Listar solo pacientes activos
     * Accesible para ADMIN y MEDICO
     */
    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<PacienteResponse>> listarActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PacienteResponse> response = pacienteService.listarActivos(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/estado/{estado} - Listar pacientes por estado
     * Accesible para ADMIN
     */
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PacienteResponse>> listarPorEstado(
            @PathVariable EstadoUsuario estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PacienteResponse> response = pacienteService.listarPorEstado(estado, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/buscar - Buscar pacientes por nombre o apellido
     * Parámetro: q (query string)
     * Accesible para ADMIN y MEDICO
     */
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<PacienteResponse>> buscar(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("apellido").ascending());
        Page<PacienteResponse> response = pacienteService.buscarPorNombreOApellido(q, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/pacientes/{id} - Actualizar datos del paciente
     * Accesible para ADMIN y el propio PACIENTE
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (#id == authentication.principal.id and hasRole('PACIENTE'))")
    public ResponseEntity<PacienteResponse> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteUpdateRequest request) {
        
        PacienteResponse response = pacienteService.actualizarPaciente(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/pacientes/{id}/estado - Cambiar estado del paciente
     * Accesible solo para ADMIN
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoUsuario estado) {
        
        PacienteResponse response = pacienteService.cambiarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/pacientes/{id} - Eliminar (desactivar) paciente
     * Accesible solo para ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Paciente desactivado exitosamente");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/pacientes/stats/count - Obtener estadísticas de pacientes por estado
     * Accesible para ADMIN
     */
    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("activos", pacienteService.contarPorEstado(EstadoUsuario.ACTIVO));
        stats.put("inactivos", pacienteService.contarPorEstado(EstadoUsuario.INACTIVO));
        stats.put("suspendidos", pacienteService.contarPorEstado(EstadoUsuario.SUSPENDIDO));
        
        return ResponseEntity.ok(stats);
    }
}