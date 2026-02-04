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

    /**
     * Construye el turno al recibir los parámetros correspondientes.
     * Crea el turno con estado PENDIENTE por defecto.
     *
     * Valida que el paciente y el médico existan.
     * Valida que el médico tenga disponibilidad en esa fecha y hora.
     *
     * @param pacienteId ID del paciente
     * @param medicoId ID del médico
     * @param fecha fecha del turno
     * @param hora hora del turno
     * @param motivo motivo de consulta
     *
     * @return Turno creado y guardado
     *
     * @throws RuntimeException si el médico ya tiene turno en ese horario
     */
    public Turno agendarTurno(Long pacienteId, Long medicoId, LocalDate fecha, LocalDateTime hora, String motivo){

        // Busca el paciente o lanza error si no existe
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Busca el médico o lanza error si no existe
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));

        // Verifica si el médico ya tiene un turno en ese horario
        if (!validarDisponibilidad(medicoId, fecha, hora)) {
            throw new RuntimeException("El medico ya tiene un turno en ese horario");
        }

        // Construccion del turno
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setMedico(medico);
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEstado(EstadoTurno.PENDIENTE);
        turno.setMotivo(motivo);

        return turnoRepository.save(turno);
    }

    /**
     * Verifica si un médico tiene disponible una fecha y hora.
     *
     * @param medicoId ID del médico
     * @param fecha fecha del turno
     * @param hora hora del turno
     *
     * @return true si NO existe turno (está disponible), false si está ocupado
     */
    public boolean validarDisponibilidad(Long medicoId, LocalDate fecha, LocalDateTime hora) {
        return !turnoRepository.existsByMedicoIdAndFechaAndHora(medicoId, fecha, hora);
    }

    /**
     * Cambia el estado de un turno a CANCELADO.
     *
     * @param turnoId ID del turno
     * @return Turno actualizado
     */
    public Turno cancelarTurno(Long turnoId){
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.CANCELADO);
        return turnoRepository.save(t);
    }

    /**
     * Cambia el estado de un turno a CONFIRMADO.
     *
     * @param turnoId ID del turno
     * @return Turno actualizado
     */
    public Turno confirmarTurno(Long turnoId) {
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.CONFIRMADO);
        return turnoRepository.save(t);
    }

    /**
     * Cambia el estado de un turno a COMPLETADO.
     *
     * @param turnoId ID del turno
     * @return Turno actualizado
     */
    public Turno completarTurno(Long turnoId) {
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.COMPLETADO);
        return turnoRepository.save(t);
    }

    /**
     * Cambia el estado de un turno a AUSENTE.
     *
     * @param turnoId ID del turno
     * @return Turno actualizado
     */
    public Turno ausentarTurno(Long turnoId){
        Turno t = getTurnoPorId(turnoId);
        t.setEstado(EstadoTurno.AUSENTE);
        return turnoRepository.save(t);
    }

    /**
     * Busca y retorna un turno por su ID.
     * Carga el turno con sus relaciones (paciente, médico) para evitar LazyInitializationException.
     *
     * @param turnoId ID del turno
     * @return Turno encontrado
     *
     * @throws RuntimeException si no existe
     */
    public Turno getTurnoPorId(Long turnoId){
        return turnoRepository.findByIdWithRelations(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }

    /**
     * Lista todos los turnos de un paciente.
     *
     * @param pacienteId ID del paciente
     * @return lista de turnos
     */
    public List<Turno> listarTurnosDePaciente(Long pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId);
    }

    /**
     * Lista todos los turnos de un médico.
     *
     * @param medicoId ID del médico
     * @return lista de turnos
     */
    public List<Turno> listarTurnosDeMedico(Long medicoId) {
        return turnoRepository.findByMedicoId(medicoId);
    }

    /**
     * Lista todos los turnos en una fecha específica.
     *
     * @param fecha fecha buscada
     * @return lista de turnos en esa fecha
     */
    public List<Turno> listarTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findByFecha(fecha);
    }

    /**
     * Lista todos los turnos por estado.
     *
     * @param estado estado del turno
     * @return lista de turnos
     */
    public List<Turno> listarTurnosPorEstado(EstadoTurno estado) {
        return turnoRepository.findByEstado(estado);
    }

    /**
     * Lista los próximos turnos de un médico tomando como base la fecha actual.
     *
     * @param medicoId ID del médico
     * @return lista de turnos futuros
     */
    public List<Turno> listarProximosTurnosDeMedico(Long medicoId) {
        return turnoRepository.findProximosTurnosDeMedico(medicoId, LocalDate.now());
    }

    /**
     * Cuenta cuántos turnos tiene un médico en una fecha específica.
     *
     * @param medicoId ID del médico
     * @param fecha fecha a consultar
     * @return cantidad de turnos
     */
    public Long contarTurnosDelMedicoEnFecha(Long medicoId, LocalDate fecha) {
        return turnoRepository.contarTurnosPorMedicoYFecha(medicoId, fecha);
    }

    /**
     * Lista el historial completo de un paciente.
     *
     * @param pacienteId ID del paciente
     * @return lista de turnos del paciente
     */
    public List<Turno> listarHistorialDePaciente(Long pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId);
    }
}