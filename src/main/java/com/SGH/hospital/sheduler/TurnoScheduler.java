package com.SGH.hospital.sheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.SGH.hospital.mapper.TurnoMapper;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.repository.TurnoRepository;
import com.SGH.hospital.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TurnoScheduler {
    
    private final TurnoRepository turnoRepository;
    private final  EmailService emailService;
    private final TurnoMapper turnoMapper;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void enviarRecordatorios() {
        LocalDateTime desde = LocalDateTime.now().plusHours(23);
        LocalDateTime hasta = LocalDateTime.now().plusHours(25);

        List<Turno> turnos = turnoRepository.findTurnosParaRecordatorio24h(desde, hasta, EstadoTurno.PENDIENTE);

        turnos.forEach(turno ->{
            emailService.enviarRecordatorioMedico(turnoMapper.toDTO(turno));
            turno.setRecordatorio24hEnviado(true);
        });
        turnoRepository.saveAll(turnos);
    }

    @Transactional
    @Scheduled(cron = "0 */10 * * * *")
    public void marcarAusentes() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(30);

        List<Turno> turnos = turnoRepository.findTurnosParaAusentes(limite);

        turnos.forEach(turno -> {
            turno.setEstado(EstadoTurno.AUSENTE);
        });
        turnoRepository.saveAll(turnos);
        log.info("Turnos marcados como ausentes: {}", turnos.size());
    }
}
