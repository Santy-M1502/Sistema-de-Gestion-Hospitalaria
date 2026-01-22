package com.SGH.hospital.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SGH.hospital.entity.Medico;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.repository.MedicoRepository;
import com.SGH.hospital.repository.PacienteRepository;
import com.SGH.hospital.repository.TurnoRepository;

@Service
public class TurnoService {
    
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public Turno agendarTurno(Long pacienteId, Long medicoId, LocalDate fecha, LocalDateTime hora, String motivo){

        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(medicoId).orElseThrow(() -> new RuntimeException("Medico no encontrado"));

        if(!validarDisponibilidad(medicoId, fecha, hora)){
            throw new RuntimeException("El medico ya tiene un turno en ese horario");
        }

        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setMedico(medico);
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEstado(EstadoTurno.PENDIENTE);
        turno.setMotivo(motivo);

        return turnoRepository.save(turno);
    }

     public boolean validarDisponibilidad(Long medicoId, LocalDate fecha, LocalDateTime hora) {
        return !turnoRepository.existsByMedicoIdAndFechaAndHora(medicoId, fecha, hora);
    }

    public Turno cancelarTurno(Long turnoId){
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.CANCELADO);
        return turnoRepository.save(t);
    }

    public Turno confirmarTurno(Long turnoId) {
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.CONFIRMADO);
        return turnoRepository.save(t);
    }

    public Turno completarTurno(Long turnoId) {
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.COMPLETADO);
        return turnoRepository.save(t);
    }

    public Turno ausentarTurno(Long turnoId){
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.AUSENTE);
        return turnoRepository.save(t);
    }

    public Turno getTurnoPorId(Long turnoId){
        return turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }

    public List<Turno> listarTurnosDePaciente(Long pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId);
    }

    public List<Turno> listarTurnosDeMedico(Long medicoId) {
        return turnoRepository.findByMedicoId(medicoId);
    }

    public List<Turno> listarTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findByFecha(fecha);
    }

    public List<Turno> listarTurnosPorEstado(EstadoTurno estado) {
        return turnoRepository.findByEstado(estado);
    }

    public List<Turno> listarProximosTurnosDeMedico(Long medicoId) {
        return turnoRepository.findProximosTurnosDeMedico(medicoId, LocalDate.now());
    }

    public Long contarTurnosDelMedicoEnFecha(Long medicoId, LocalDate fecha) {
        return turnoRepository.contarTurnosPorMedicoYFecha(medicoId, fecha);
    }

    public List<Turno> listarHistorialDePaciente(Long pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId);
    }
}
