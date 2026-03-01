package com.SGH.hospital.controller;

import com.SGH.hospital.entity.Consulta;
import com.SGH.hospital.entity.SignosVitales;
import com.SGH.hospital.service.ConsultaService;
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

    // -----------------------------------------------
    // CRUD Consulta
    // -----------------------------------------------

    /**
     * POST /api/consultas
     * Crea una consulta asociada a un turno existente.
     */
    @PostMapping
    public ResponseEntity<Consulta> crearConsulta(
            @RequestParam Long turnoId,
            @RequestParam Long medicoId,
            @RequestParam String motivo,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) String tratamiento,
            @RequestBody(required = false) SignosVitales signosVitales) {

        Consulta consulta = consultaService.crearConsulta(
                turnoId, medicoId, motivo, diagnostico, observaciones, tratamiento, signosVitales);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    /**
     * GET /api/consultas/{id}
     * Busca una consulta por su ID con todas sus relaciones.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    /**
     * GET /api/consultas/turno/{turnoId}
     * Busca la consulta asociada a un turno.
     */
    @GetMapping("/turno/{turnoId}")
    public ResponseEntity<Consulta> buscarPorTurno(@PathVariable Long turnoId) {
        return ResponseEntity.ok(consultaService.buscarPorTurnoId(turnoId));
    }

    /**
     * GET /api/consultas/medico/{medicoId}
     * Lista todas las consultas de un médico.
     */
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Consulta>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(consultaService.listarConsultasDeMedico(medicoId));
    }

    /**
     * GET /api/consultas/paciente/{pacienteId}
     * Lista todas las consultas de un paciente.
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Consulta>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarConsultasDePaciente(pacienteId));
    }

    /**
     * GET /api/consultas/fechas?desde=2024-01-01T00:00:00&hasta=2024-12-31T23:59:59
     * Lista consultas dentro de un rango de fechas.
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Consulta>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        return ResponseEntity.ok(consultaService.listarConsultasPorFecha(desde, hasta));
    }

    /**
     * GET /api/consultas/medico/{medicoId}/periodo?desde=...&hasta=...
     * Lista consultas de un médico en un período específico.
     */
    @GetMapping("/medico/{medicoId}/periodo")
    public ResponseEntity<List<Consulta>> listarPorMedicoYPeriodo(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        return ResponseEntity.ok(consultaService.listarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta));
    }

    /**
     * GET /api/consultas/diagnostico?descripcion=diabetes
     * Busca consultas por diagnóstico.
     */
    @GetMapping("/diagnostico")
    public ResponseEntity<List<Consulta>> buscarPorDiagnostico(@RequestParam String descripcion) {
        return ResponseEntity.ok(consultaService.buscarPorDiagnostico(descripcion));
    }

    /**
     * GET /api/consultas/medico/{medicoId}/count?desde=...&hasta=...
     * Cuenta cuántas consultas tuvo un médico en un período.
     */
    @GetMapping("/medico/{medicoId}/count")
    public ResponseEntity<Long> contarConsultasDeMedico(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        return ResponseEntity.ok(consultaService.contarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta));
    }

    /**
     * PATCH /api/consultas/{id}/diagnostico
     * Actualiza el diagnóstico, tratamiento y observaciones de una consulta.
     */
    @PatchMapping("/{id}/diagnostico")
    public ResponseEntity<Consulta> actualizarDiagnostico(
            @PathVariable Long id,
            @RequestParam String diagnostico,
            @RequestParam(required = false) String tratamiento,
            @RequestParam(required = false) String observaciones) {

        return ResponseEntity.ok(consultaService.actualizarDiagnostico(id, diagnostico, tratamiento, observaciones));
    }

    /**
     * DELETE /api/consultas/{id}
     * Elimina una consulta.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConsulta(@PathVariable Long id) {
        consultaService.eliminarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}