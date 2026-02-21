package com.SGH.hospital.controller;

import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.service.TurnoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.SGH.hospital.dto.turno.TurnoResponse;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    // ============================================================
    // Crear/agendar turno
    // ============================================================

    @PostMapping("/agendar")
    public TurnoResponse agendarTurno(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hora,
            @RequestParam String motivo
    ) {
        return new TurnoResponse(turnoService.agendarTurno(pacienteId, medicoId, fecha, hora, motivo));
    }

    // ============================================================
    // Acciones sobre un turno
    // ============================================================

    @PutMapping("/{id}/cancelar")
    public TurnoResponse cancelarTurno(@PathVariable Long id) {
        return new TurnoResponse(turnoService.cancelarTurno(id));
    }

    @PutMapping("/{id}/confirmar")
    public TurnoResponse confirmarTurno(@PathVariable Long id) {
        return new TurnoResponse(turnoService.confirmarTurno(id));
    }

    @PutMapping("/{id}/completar")
    public TurnoResponse completarTurno(@PathVariable Long id) {
        return new TurnoResponse(turnoService.completarTurno(id));
    }

    @PutMapping("/{id}/ausente")
    public TurnoResponse marcarAusente(@PathVariable Long id) {
        return new TurnoResponse(turnoService.ausentarTurno(id));
    }

    // ============================================================
    // Búsquedas
    // ============================================================

    @GetMapping("/{id}")
    public TurnoResponse getTurnoPorId(@PathVariable Long id) {
        return new TurnoResponse(turnoService.getTurnoPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<TurnoResponse> listarTurnosDePaciente(@PathVariable Long pacienteId) {
        return turnoService.listarTurnosDePaciente(pacienteId).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/medico/{medicoId}")
    public List<TurnoResponse> listarTurnosDeMedico(@PathVariable Long medicoId) {
        return turnoService.listarTurnosDeMedico(medicoId).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/fecha")
    public List<TurnoResponse> listarTurnosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return turnoService.listarTurnosPorFecha(fecha).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/estado")
    public List<TurnoResponse> listarTurnosPorEstado(@RequestParam EstadoTurno estado) {
        return turnoService.listarTurnosPorEstado(estado).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/medico/{medicoId}/proximos")
    public List<TurnoResponse> listarProximosTurnosDeMedico(@PathVariable Long medicoId) {
        return turnoService.listarProximosTurnosDeMedico(medicoId).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/medico/{medicoId}/contar")
    public Long contarTurnosPorMedicoYFecha(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return turnoService.contarTurnosDelMedicoEnFecha(medicoId, fecha);
    }

    @GetMapping("/paciente/{pacienteId}/historial")
    public List<TurnoResponse> historialPaciente(@PathVariable Long pacienteId) {
        return turnoService.listarHistorialDePaciente(pacienteId).stream()
                .map(TurnoResponse::new)
                .collect(Collectors.toList());
    }
}