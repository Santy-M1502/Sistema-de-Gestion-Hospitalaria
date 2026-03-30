package com.SGH.hospital.controller;

import com.SGH.hospital.entity.HistoriaClinica;
import com.SGH.hospital.entity.Antecedente;
import com.SGH.hospital.enums.GrupoSanguineo;
import com.SGH.hospital.service.AntecedenteService;
import com.SGH.hospital.service.HistoriaClinicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historias-clinicas")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;
    private final AntecedenteService antecedenteService;

    public HistoriaClinicaController(HistoriaClinicaService historiaClinicaService,
                                      AntecedenteService antecedenteService) {
        this.historiaClinicaService = historiaClinicaService;
        this.antecedenteService = antecedenteService;
    }

    // -----------------------------------------------
    // CRUD Historia Clínica
    // -----------------------------------------------

    /**
     * POST /api/historias-clinicas
     * Crea una historia clínica para un paciente.
     */
    @PostMapping
    public ResponseEntity<HistoriaClinica> crearHistoriaClinica(
            @RequestParam Long pacienteId,
            @RequestParam(required = false) GrupoSanguineo grupoSanguineo,
            @RequestParam(required = false) String alergias) {

        HistoriaClinica hc = historiaClinicaService.crearHistoriaClinica(pacienteId, grupoSanguineo, alergias);
        return ResponseEntity.status(HttpStatus.CREATED).body(hc);
    }

    /**
     * GET /api/historias-clinicas/{id}
     * Busca una historia clínica por su ID con todas sus relaciones.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinica> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorId(id));
    }

    /**
     * GET /api/historias-clinicas/paciente/{pacienteId}
     * Busca la historia clínica de un paciente específico.
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<HistoriaClinica> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorPacienteId(pacienteId));
    }

    /**
     * GET /api/historias-clinicas/grupo-sanguineo/{grupo}
     * Lista todas las historias clínicas de un grupo sanguíneo.
     */
    @GetMapping("/grupo-sanguineo/{grupo}")
    public ResponseEntity<List<HistoriaClinica>> buscarPorGrupoSanguineo(@PathVariable GrupoSanguineo grupo) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorGrupoSanguineo(grupo));
    }

    /**
     * GET /api/historias-clinicas/alergia?descripcion=penicilina
     * Busca historias clínicas que contengan cierta alergia.
     */
    @GetMapping("/alergia")
    public ResponseEntity<List<HistoriaClinica>> buscarPorAlergia(@RequestParam String descripcion) {
        return ResponseEntity.ok(historiaClinicaService.buscarPorAlergia(descripcion));
    }

    /**
     * PATCH /api/historias-clinicas/{id}/alergias
     * Actualiza las alergias de una historia clínica.
     */
    @PatchMapping("/{id}/alergias")
    public ResponseEntity<HistoriaClinica> actualizarAlergias(
            @PathVariable Long id,
            @RequestParam String alergias) {

        return ResponseEntity.ok(historiaClinicaService.actualizarAlergias(id, alergias));
    }

    /**
     * PATCH /api/historias-clinicas/{id}/grupo-sanguineo
     * Actualiza el grupo sanguíneo de una historia clínica.
     */
    @PatchMapping("/{id}/grupo-sanguineo")
    public ResponseEntity<HistoriaClinica> actualizarGrupoSanguineo(
            @PathVariable Long id,
            @RequestParam GrupoSanguineo grupoSanguineo) {

        return ResponseEntity.ok(historiaClinicaService.actualizarGrupoSanguineo(id, grupoSanguineo));
    }

    /**
     * DELETE /api/historias-clinicas/{id}
     * Elimina una historia clínica.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistoriaClinica(@PathVariable Long id) {
        historiaClinicaService.eliminarHistoriaClinica(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------
    // Antecedentes dentro de Historia Clínica
    // -----------------------------------------------

    /**
     * POST /api/historias-clinicas/{id}/antecedentes
     * Agrega un antecedente a una historia clínica.
     */
    @PostMapping("/{id}/antecedentes")
    public ResponseEntity<Antecedente> agregarAntecedente(
            @PathVariable Long id,
            @RequestBody Antecedente antecedente) {

        HistoriaClinica historia = historiaClinicaService.buscarPorId(id); // ← guardás la HC
        antecedente.setId(null);          // ← forzás INSERT, nunca UPDATE
        antecedente.setHistoriaClinica(historia);  // ← vinculás el antecedente a la HC
        
        Antecedente guardado = antecedenteService.guardarAntecedente(antecedente);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * DELETE /api/historias-clinicas/{id}/antecedentes/{antecedenteId}
     * Elimina un antecedente de una historia clínica.
     */
    @DeleteMapping("/{id}/antecedentes/{antecedenteId}")
    public ResponseEntity<Void> eliminarAntecedente(
            @PathVariable Long id,
            @PathVariable Long antecedenteId) {

        historiaClinicaService.buscarPorId(id);
        antecedenteService.eliminarAntecedente(antecedenteId);
        return ResponseEntity.noContent().build();
    }
}