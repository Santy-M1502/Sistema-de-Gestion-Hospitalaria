package com.SGH.hospital.controller;

import com.SGH.hospital.dto.consulta.ConsultaRequest;
import com.SGH.hospital.dto.consulta.ConsultaResponse;
import com.SGH.hospital.service.ConsultaService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> crearConsulta(@Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crearConsulta(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/turno/{turnoId}")
    public ResponseEntity<ConsultaResponse> buscarPorTurno(@PathVariable Long turnoId) {
        return ResponseEntity.ok(consultaService.buscarPorTurnoId(turnoId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ConsultaResponse>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(consultaService.listarConsultasDeMedico(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarConsultasDePaciente(pacienteId));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<ConsultaResponse>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(consultaService.listarConsultasPorFecha(desde, hasta));
    }

    @GetMapping("/medico/{medicoId}/periodo")
    public ResponseEntity<List<ConsultaResponse>> listarPorMedicoYPeriodo(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(consultaService.listarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta));
    }

    @GetMapping("/diagnostico")
    public ResponseEntity<List<ConsultaResponse>> buscarPorDiagnostico(@RequestParam String descripcion) {
        return ResponseEntity.ok(consultaService.buscarPorDiagnostico(descripcion));
    }

    @GetMapping("/medico/{medicoId}/count")
    public ResponseEntity<Long> contarConsultasDeMedico(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(consultaService.contarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta));
    }

    @PatchMapping("/{id}/diagnostico")
    public ResponseEntity<ConsultaResponse> actualizarDiagnostico(
            @PathVariable Long id,
            @RequestParam String diagnostico,
            @RequestParam(required = false) String tratamiento,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(consultaService.actualizarDiagnostico(id, diagnostico, tratamiento, observaciones));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConsulta(@PathVariable Long id) {
        consultaService.eliminarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}