package com.SGH.hospital.service;

import com.SGH.hospital.dto.consulta.ConsultaRequest;
import com.SGH.hospital.dto.consulta.ConsultaResponse;
import com.SGH.hospital.entity.Consulta;
import com.SGH.hospital.entity.SignosVitales;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;
import com.SGH.hospital.exception.BadRequestException;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.mapper.ConsultaMapper;
import com.SGH.hospital.repository.ConsultaRepository;
import com.SGH.hospital.repository.TurnoRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final TurnoRepository turnoRepository;
    private final ConsultaMapper consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository,
                           TurnoRepository turnoRepository,
                           ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.turnoRepository = turnoRepository;
        this.consultaMapper = consultaMapper;
    }

    @Transactional
    public ConsultaResponse crearConsulta(ConsultaRequest request) {

        Turno turno = turnoRepository.findById(request.getTurnoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Turno con id " + request.getTurnoId() + " no encontrado"));

        // El médico viene del turno, no del request
        if (turno.getMedico() == null) {
            throw new BadRequestException("El turno no tiene un médico asignado");
        }

        // Validar estado del turno
        if (turno.getEstado() != EstadoTurno.CONFIRMADO) {
            throw new BadRequestException("Solo se puede crear una consulta para turnos confirmados");
        }

        if (consultaRepository.existsByTurnoId(request.getTurnoId())) {
            throw new BadRequestException("El turno ya tiene una consulta registrada");
        }

        SignosVitales signosVitales = null;
        if (request.getSignosVitales() != null) {
            signosVitales = new SignosVitales();
            signosVitales.setPresionArterial(request.getSignosVitales().getPresionArterial());
            signosVitales.setFrecuenciaCardiaca(request.getSignosVitales().getFrecuenciaCardiaca());
            signosVitales.setTemperatura(request.getSignosVitales().getTemperatura());
            signosVitales.setPeso(request.getSignosVitales().getPeso());
            signosVitales.setAltura(request.getSignosVitales().getAltura());
            signosVitales.setSaturacionOxigeno(request.getSignosVitales().getSaturacionOxigeno());
        }

        Consulta consulta = new Consulta();
        consulta.setTurno(turno);
        consulta.setMedico(turno.getMedico());   // siempre del turno
        consulta.setFecha(LocalDateTime.now());  // siempre del servidor
        consulta.setMotivo(request.getMotivo());
        consulta.setDiagnostico(request.getDiagnostico());
        consulta.setObservaciones(request.getObservaciones());
        consulta.setTratamiento(request.getTratamiento());
        consulta.setSignosVitales(signosVitales);

        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    public ConsultaResponse buscarPorId(Long id) {
        return consultaMapper.toResponse(
                consultaRepository.findByIdWithRelations(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Consulta con id " + id + " no encontrada")));
    }

    public ConsultaResponse buscarPorTurnoId(Long turnoId) {
        return consultaMapper.toResponse(
                consultaRepository.findByTurnoId(turnoId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No existe consulta para el turno con id " + turnoId)));
    }

    public List<ConsultaResponse> listarConsultasDeMedico(Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    public List<ConsultaResponse> listarConsultasDePaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    public List<ConsultaResponse> listarConsultasPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return consultaRepository.findByFechaBetween(desde, hasta)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    public List<ConsultaResponse> listarConsultasDeMedicoEnPeriodo(Long medicoId,
                                                                    LocalDateTime desde,
                                                                    LocalDateTime hasta) {
        return consultaRepository.findByMedicoIdAndFechaBetween(medicoId, desde, hasta)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    public List<ConsultaResponse> buscarPorDiagnostico(String diagnostico) {
        return consultaRepository.findByDiagnosticoContainingIgnoreCase(diagnostico)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    public Long contarConsultasDeMedicoEnPeriodo(Long medicoId, LocalDateTime desde, LocalDateTime hasta) {
        return consultaRepository.contarConsultasDeMedicoEnPeriodo(medicoId, desde, hasta);
    }

    public ConsultaResponse actualizarDiagnostico(Long id, String diagnostico,
                                                   String tratamiento, String observaciones) {
        Consulta consulta = consultaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consulta con id " + id + " no encontrada"));

        consulta.setDiagnostico(diagnostico);
        consulta.setTratamiento(tratamiento);
        consulta.setObservaciones(observaciones);

        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    public void eliminarConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta con id " + id + " no encontrada");
        }
        consultaRepository.deleteById(id);
    }
}