package com.SGH.hospital.controller;

import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.service.TurnoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    // ============================================================
    // Crear/agendar turno
    // ============================================================

    @PostMapping("/agendar")
    public Turno agendarTurno(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hora,
            @RequestParam String motivo
    ) {
        return turnoService.agendarTurno(pacienteId, medicoId, fecha, hora, motivo);
    }

    // ============================================================
    // Acciones sobre un turno
    // ============================================================

    @PutMapping("/{id}/cancelar")
    public Turno cancelarTurno(@PathVariable Long id) {
        return turnoService.cancelarTurno(id);
    }

    @PutMapping("/{id}/confirmar")
    public Turno confirmarTurno(@PathVariable Long id) {
        return turnoService.confirmarTurno(id);
    }

    @PutMapping("/{id}/completar")
    public Turno completarTurno(@PathVariable Long id) {
        return turnoService.completarTurno(id);
    }

    @PutMapping("/{id}/ausente")
    public Turno marcarAusente(@PathVariable Long id) {
        return turnoService.ausentarTurno(id);
    }

    // ============================================================
    // Búsquedas
    // ============================================================

    @GetMapping("/{id}")
    public Turno getTurnoPorId(@PathVariable Long id) {
        return turnoService.getTurnoPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Turno> listarTurnosDePaciente(@PathVariable Long pacienteId) {
        return turnoService.listarTurnosDePaciente(pacienteId);
    }

    @GetMapping("/medico/{medicoId}")
    public List<Turno> listarTurnosDeMedico(@PathVariable Long medicoId) {
        return turnoService.listarTurnosDeMedico(medicoId);
    }

    @GetMapping("/fecha")
    public List<Turno> listarTurnosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return turnoService.listarTurnosPorFecha(fecha);
    }

    @GetMapping("/estado")
    public List<Turno> listarTurnosPorEstado(@RequestParam EstadoTurno estado) {
        return turnoService.listarTurnosPorEstado(estado);
    }

    @GetMapping("/medico/{medicoId}/proximos")
    public List<Turno> listarProximosTurnosDeMedico(@PathVariable Long medicoId) {
        return turnoService.listarProximosTurnosDeMedico(medicoId);
    }

    @GetMapping("/medico/{medicoId}/contar")
    public Long contarTurnosPorMedicoYFecha(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return turnoService.contarTurnosDelMedicoEnFecha(medicoId, fecha);
    }

    @GetMapping("/paciente/{pacienteId}/historial")
    public List<Turno> historialPaciente(@PathVariable Long pacienteId) {
        return turnoService.listarHistorialDePaciente(pacienteId);
    }
}