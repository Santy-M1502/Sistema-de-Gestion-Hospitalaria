package com.SGH.hospital.controller;

import com.SGH.hospital.dto.signosVitales.signosVitalesRequest;
import com.SGH.hospital.dto.signosVitales.signosVitalesResponse;
import com.SGH.hospital.service.SignosVitalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signos-vitales")
public class SignosVitalesController {

    private final SignosVitalesService signosVitalesService;

    public SignosVitalesController(SignosVitalesService signosVitalesService) {
        this.signosVitalesService = signosVitalesService;
    }

    @PostMapping
    public ResponseEntity<signosVitalesResponse> guardar(@Valid @RequestBody signosVitalesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signosVitalesService.guardar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<signosVitalesResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(signosVitalesService.buscarPorId(id));
    }

    @GetMapping("/fiebre")
    public ResponseEntity<List<signosVitalesResponse>> conFiebre(
            @RequestParam(defaultValue = "37.5") Double umbral) {
        return ResponseEntity.ok(signosVitalesService.buscarConFiebre(umbral));
    }

    @GetMapping("/hipoxia")
    public ResponseEntity<List<signosVitalesResponse>> conHipoxia(
            @RequestParam(defaultValue = "95") Integer umbral) {
        return ResponseEntity.ok(signosVitalesService.buscarConHipoxia(umbral));
    }

    @GetMapping("/frecuencia-anormal")
    public ResponseEntity<List<signosVitalesResponse>> frecuenciaAnormal() {
        return ResponseEntity.ok(signosVitalesService.buscarFrecuenciaAnormal());
    }

    @GetMapping("/sobrepeso")
    public ResponseEntity<List<signosVitalesResponse>> conSobrepeso() {
        return ResponseEntity.ok(signosVitalesService.buscarConSobrepeso());
    }

    @GetMapping("/imc")
    public ResponseEntity<List<signosVitalesResponse>> porRangoIMC(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(signosVitalesService.buscarPorRangoIMC(min, max));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        signosVitalesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}