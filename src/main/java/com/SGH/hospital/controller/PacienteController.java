package com.SGH.hospital.controller;

import com.SGH.hospital.dto.paciente.PacienteRequest;
import com.SGH.hospital.dto.paciente.PacienteResponse;
import com.SGH.hospital.dto.paciente.PacienteUpdateRequest;
import com.SGH.hospital.entity.Consulta;
import com.SGH.hospital.entity.HistoriaClinica;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.service.ConsultaService;
import com.SGH.hospital.service.HistoriaClinicaService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final HistoriaClinicaService historiaClinicaService;
    private final ConsultaService consultaService;

    // ============================================================
    // Crear paciente
    // ============================================================

    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.crearPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<PacienteResponse> obtenerPorId(@PathVariable Long id) {
        PacienteResponse response = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<PacienteResponse> obtenerPorDni(@PathVariable String dni) {
        PacienteResponse response = pacienteService.obtenerPorDni(dni);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteResponse> obtenerPorEmail(@PathVariable String email) {
        PacienteResponse response = pacienteService.obtenerPorEmail(email);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Listados y busquedas
    // ============================================================

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

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<PacienteResponse>> listarActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PacienteResponse> response = pacienteService.listarActivos(pageable);
        return ResponseEntity.ok(response);
    }

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

    // ============================================================
    // Actualizacion de datos
    // ============================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (#id == authentication.principal.id and hasRole('PACIENTE'))")
    public ResponseEntity<PacienteResponse> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteUpdateRequest request) {
        
        PacienteResponse response = pacienteService.actualizarPaciente(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Cambio de estado
    // ============================================================

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoUsuario estado) {
        
        PacienteResponse response = pacienteService.cambiarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Eliminacion / Desactivacion
    // ============================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Paciente desactivado exitosamente");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Estadisticas
    // ============================================================

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("activos", pacienteService.contarPorEstado(EstadoUsuario.ACTIVO));
        stats.put("inactivos", pacienteService.contarPorEstado(EstadoUsuario.INACTIVO));
        stats.put("suspendidos", pacienteService.contarPorEstado(EstadoUsuario.SUSPENDIDO));
        
        return ResponseEntity.ok(stats);
    }

    // ============================================================
    // DatosPaciente
    // ============================================================

    @GetMapping("/{id}/historia-clinica")
    public ResponseEntity<HistoriaClinica> getHistoriaClinica(@PathVariable Long id) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorPacienteId(id));
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<Consulta>> getConsultas(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.listarConsultasDePaciente(id));
    }
}