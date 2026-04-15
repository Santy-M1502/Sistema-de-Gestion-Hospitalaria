package com.SGH.hospital.controller;

import com.SGH.hospital.entity.ListaEspera;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.service.ListaEsperaService;
import com.SGH.hospital.repository.TurnoRepository;
import com.SGH.hospital.dto.listaEspera.ListaEsperaResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/lista-espera")
public class ListaEsperaController {

    @Autowired
    private ListaEsperaService listaEsperaService;

    @Autowired
    private TurnoRepository turnoRepository;

    // Agregar paciente a la lista
    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<?> agregar(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSolicitada
    ) {
        listaEsperaService.agregarALaLista(pacienteId, medicoId, fechaSolicitada);
        return ResponseEntity.ok().build();
    }

    // Obtener lista de un día
    @GetMapping("/dia")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<ListaEsperaResponse>> obtenerPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Pageable pageable
    ) {
        Page<ListaEsperaResponse> response =
                listaEsperaService.obtenerListaDelDia(fecha, pageable);

        return ResponseEntity.ok(response);
    }

    // Obtener el siguiente paciente en la lista
    @GetMapping("/siguiente")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<?> siguiente(@RequestParam Long medicoId) {
        ListaEspera siguiente = listaEsperaService.obtenerSiguiente(medicoId);
        if (siguiente == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ListaEsperaResponse(siguiente));
    }

    // Ejecutar la lógica cuando un turno se cancela
    @PostMapping("/turno-cancelado")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public String turnoCancelado(@RequestParam Long turnoId) {
        listaEsperaService.manejarTurnoCancelado(turnoId);
        return "Procesado";
    }

    // ---- ENDPOINTS DE NOTIFICACIONES DE PRUEBA ---- //

    // Confirmación
    @PostMapping("/notificacion/confirmacion")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public String confirmar(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findByIdWithRelations(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.turnoConfirmado(turno);
        return "Notificación de confirmación enviada";
    }

    // Aviso 24h
    @PostMapping("/notificacion/24h")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public String aviso24(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findByIdWithRelations(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.aviso24(turno);
        return "Aviso 24h enviado";
    }

    // Aviso 2h
    @PostMapping("/notificacion/2h")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public String aviso2(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findByIdWithRelations(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.aviso2(turno);
        return "Aviso 2h enviado";
    }
}