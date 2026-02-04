package com.SGH.hospital.service;

import com.SGH.hospital.dto.listaEspera.ListaEsperaResponse;
import com.SGH.hospital.entity.*;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.enums.TipoNotificacion;
import com.SGH.hospital.repository.*;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ListaEsperaService {

    @Autowired
    private ListaEsperaRepository listaEsperaRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private NotificacionService notificacionService;

    // Agregar paciente a lista de espera
    public ListaEspera agregarALaLista(Long pacienteId, Long medicoId, LocalDate fechaSolicitada) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        ListaEspera entrada = new ListaEspera(paciente, medico, fechaSolicitada);
        return listaEsperaRepository.save(entrada);
    }

    // Obtener próximo paciente en la lista
    public ListaEspera obtenerSiguiente(Long medicoId) {
        return listaEsperaRepository.findFirstByMedicoIdOrderByIdAsc(medicoId);
    }

    // Ejecutado cuando un turno se cancela
    public void manejarTurnoCancelado(Long turnoCanceladoId) {

        Turno turno = turnoRepository.findById(turnoCanceladoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        Long medicoId = turno.getMedico().getId();

        // Buscar siguiente paciente en la lista
        ListaEspera siguiente = obtenerSiguiente(medicoId);

        if (siguiente == null) {
            // No hay nadie esperando
            System.out.println("No hay nadie en lista de espera para este médico.");
            return;
        }

        // Asignar turno al siguiente paciente
        turno.setPaciente(siguiente.getPaciente());
        turno.setEstado(EstadoTurno.CONFIRMADO);
        turnoRepository.save(turno);

        // Enviar notificación usando el nuevo sistema
        notificacionService.enviar(turno, TipoNotificacion.TURNO_DISPONIBLE);

        // Eliminar de la lista de espera
        listaEsperaRepository.deleteById(siguiente.getId());
    }

    // Obtener lista del día
    public Page<ListaEsperaResponse> obtenerListaDelDia(LocalDate fecha, Pageable pageable) {
        return listaEsperaRepository
                .findByFechaSolicitada(fecha, pageable)
                .map(ListaEsperaResponse::new);
    }

    public void turnoConfirmado(Turno turno){
        notificacionService.enviar(turno, TipoNotificacion.CONFIRMACION_TURNO);
    }

    public void aviso24(Turno turno){
        notificacionService.enviar(turno, TipoNotificacion.RECORDATORIO_24HS);
    }

    public void aviso2(Turno turno){
        notificacionService.enviar(turno, TipoNotificacion.RECORDATORIO_2HS);
    }
}
