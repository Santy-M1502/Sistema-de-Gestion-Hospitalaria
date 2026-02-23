package com.SGH.hospital.sheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.SGH.hospital.dto.turno.TurnoDTO;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.repository.TurnoRepository;
import com.SGH.hospital.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordatorioScheduler {

    private final TurnoRepository turnoRepository;
    private final EmailService emailService;

    @Transactional
    @Scheduled(fixedDelay = 10000) // se ejecuta cada 1 hora
    public void enviarRecordatorios24h() {
        LocalDateTime desde = LocalDateTime.now().plusHours(23);
        LocalDateTime hasta = LocalDateTime.now().plusHours(25);

        List<Turno> turnos = turnoRepository.findTurnosParaRecordatorio24h(desde, hasta, EstadoTurno.PENDIENTE);


        turnos.forEach(turno -> {
            emailService.enviarRecordatorio24h(convertirADTO(turno));
            turno.setRecordatorio24hEnviado(true);
            turnoRepository.save(turno);
            log.info("Recordatorio 24h enviado - turno id: {}", turno.getId());
        });
    }

    @Transactional
    @Scheduled(fixedDelay = 3600000)
    public void enviarRecordatorios2h() {
        LocalDateTime desde = LocalDateTime.now().plusHours(1);
        LocalDateTime hasta = LocalDateTime.now().plusHours(3);

        List<Turno> turnos = turnoRepository.findTurnosParaRecordatorio2h(desde, hasta, EstadoTurno.PENDIENTE);

        turnos.forEach(turno -> {
            emailService.enviarRecordatorio2h(convertirADTO(turno));
            turno.setRecordatorio2hEnviado(true);
            turnoRepository.save(turno);
        });
    }

    private TurnoDTO convertirADTO(Turno turno) {

        TurnoDTO dto = new TurnoDTO();

        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setHora(turno.getHora());
        dto.setEstado(turno.getEstado());
        dto.setMotivoConsulta(turno.getMotivo());

        dto.setPacienteId(turno.getPaciente().getId());
        dto.setPacienteNombre(turno.getPaciente().getNombre());
        dto.setPacienteApellido(turno.getPaciente().getApellido());
        dto.setPacienteEmail(turno.getPaciente().getEmail());

        dto.setMedicoId(turno.getMedico().getId());
        dto.setMedicoNombre(turno.getMedico().getNombre());
        dto.setMedicoApellido(turno.getMedico().getApellido());

        dto.setRecordatorio24hEnviado(turno.isRecordatorio24hEnviado());
        dto.setRecordatorio2hEnviado(turno.isRecordatorio2hEnviado());

        return dto;
    }
}