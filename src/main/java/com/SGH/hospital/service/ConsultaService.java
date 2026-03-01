package com.SGH.hospital.service;

import com.SGH.hospital.entity.Consulta;
import com.SGH.hospital.entity.Medico;
import com.SGH.hospital.entity.SignosVitales;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.exception.BadRequestException;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.repository.ConsultaRepository;
import com.SGH.hospital.repository.MedicoRepository;
import com.SGH.hospital.repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final TurnoRepository turnoRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                           TurnoRepository turnoRepository,
                           MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.turnoRepository = turnoRepository;
        this.medicoRepository = medicoRepository;
    }

    /**
     * Crea una consulta asociada a un turno.
     * Valida que el turno y el médico existan.
     * Valida que el turno no tenga ya una consulta registrada.
     */
    public Consulta crearConsulta(Long turnoId, Long medicoId, String motivo,
                                   String diagnostico, String observaciones,
                                   String tratamiento, SignosVitales signosVitales) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new ResourceNotFoundException("Turno con id " + turnoId + " no encontrado"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Medico con id " + medicoId + " no encontrado"));

        if (consultaRepository.existsByTurnoId(turnoId)) {
            throw new BadRequestException("El turno ya tiene una consulta registrada");
        }

        Consulta consulta = new Consulta();
        consulta.setTurno(turno);
        consulta.setMedico(medico);
        consulta.setFecha(LocalDateTime.now());
        consulta.setMotivo(motivo);
        consulta.setDiagnostico(diagnostico);
        consulta.setObservaciones(observaciones);
        consulta.setTratamiento(tratamiento);
        consulta.setSignosVitales(signosVitales);

        return consultaRepository.save(consulta);
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta con id " + id + " no encontrada"));
    }

    public Consulta buscarPorTurnoId(Long turnoId) {
        return consultaRepository.findByTurnoId(turnoId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe consulta para el turno con id " + turnoId));
    }

    public List<Consulta> listarConsultasDeMedico(Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId);
    }

    public List<Consulta> listarConsultasDePaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    public List<Consulta> listarConsultasPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return consultaRepository.findByFechaBetween(desde, hasta);
    }

    public List<Consulta> listarConsultasDeMedicoEnPeriodo(Long medicoId, LocalDateTime desde, LocalDateTime hasta) {
        return consultaRepository.findByMedicoIdAndFechaBetween(medicoId, desde, hasta);
    }

    public List<Consulta> buscarPorDiagnostico(String diagnostico) {
        return consultaRepository.findByDiagnosticoContainingIgnoreCase(diagnostico);
    }

    public Long contarConsultasDeMedicoEnPeriodo(Long medicoId, LocalDateTime desde, LocalDateTime hasta) {
        return consultaRepository.contarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta);
    }

    public Consulta actualizarDiagnostico(Long id, String diagnostico, String tratamiento, String observaciones) {
        Consulta consulta = buscarPorId(id);
        consulta.setDiagnostico(diagnostico);
        consulta.setTratamiento(tratamiento);
        consulta.setObservaciones(observaciones);
        return consultaRepository.save(consulta);
    }

    public void eliminarConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta con id " + id + " no encontrada");
        }
        consultaRepository.deleteById(id);
    }
}