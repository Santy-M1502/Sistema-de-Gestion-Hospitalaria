package com.SGH.hospital.controller;

import com.SGH.hospital.entity.ListaEspera;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.service.ListaEsperaService;
import com.SGH.hospital.repository.TurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lista-espera")
public class ListaEsperaController {

    @Autowired
    private ListaEsperaService listaEsperaService;

    @Autowired
    private TurnoRepository turnoRepository;

    // Agregar paciente a la lista
    @PostMapping("/agregar")
    public ListaEspera agregar(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSolicitada
    ) {
        return listaEsperaService.agregarALaLista(pacienteId, medicoId, fechaSolicitada);
    }

    // Obtener lista de un día
    @GetMapping("/dia")
    public List<ListaEspera> obtenerPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return listaEsperaService.obtenerListaDelDia(fecha);
    }

    // Obtener el siguiente paciente en la lista
    @GetMapping("/siguiente")
    public ListaEspera siguiente(@RequestParam Long medicoId) {
        return listaEsperaService.obtenerSiguiente(medicoId);
    }

    // Ejecutar la lógica cuando un turno se cancela
    @PostMapping("/turno-cancelado")
    public String turnoCancelado(@RequestParam Long turnoId) {
        listaEsperaService.manejarTurnoCancelado(turnoId);
        return "Procesado";
    }

    // ---- ENDPOINTS DE NOTIFICACIONES DE PRUEBA ---- //

    // Confirmación
    @PostMapping("/notificacion/confirmacion")
    public String confirmar(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.turnoConfirmado(turno);
        return "Notificación de confirmación enviada";
    }

    // Aviso 24h
    @PostMapping("/notificacion/24h")
    public String aviso24(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.aviso24(turno);
        return "Aviso 24h enviado";
    }

    // Aviso 2h
    @PostMapping("/notificacion/2h")
    public String aviso2(@RequestParam Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        listaEsperaService.aviso2(turno);
        return "Aviso 2h enviado";
    }
}