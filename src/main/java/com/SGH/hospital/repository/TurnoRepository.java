package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.EstadoTurno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    /**
     * Obtiene todos los turnos asociados a un paciente.
     *
     * @param pacienteId identificador del paciente
     * @return lista de turnos del paciente
     */
    List<Turno> findByPacienteId(Long pacienteId);

    /**
     * Obtiene todos los turnos asociados a un médico.
     *
     * @param medicoId identificador del médico
     * @return lista de turnos del médico
     */
    List<Turno> findByMedicoId(Long medicoId);

    /**
     * Obtiene los turnos correspondientes a una fecha específica.
     *
     * @param fecha día para buscar turnos
     * @return lista de turnos en esa fecha
     */
    List<Turno> findByFecha(LocalDate fecha);

    /**
     * Busca los próximos turnos de un médico, desde una fecha dada.
     * Ordenados por fecha y hora.
     *
     * @param medicoId identificador del médico
     * @param desde fecha mínima (incluida)
     * @return lista de turnos futuros del médico
     */
    @Query("""
        SELECT t FROM Turno t
        WHERE t.medico.id = :medicoId
        AND t.fecha >= :desde
        ORDER BY t.fecha, t.hora
        """)
    List<Turno> findProximosTurnosDeMedico(
        @Param("medicoId") Long medicoId,
        @Param("desde") LocalDate desde
    );

    /**
     * Obtiene los turnos que se encuentran en un estado específico.
     *
     * @param estado estado del turno
     * @return lista de turnos con ese estado
     */
    List<Turno> findByEstado(EstadoTurno estado);

    /**
     * Cuenta la cantidad de turnos que tiene un médico en una fecha específica.
     *
     * @param medicoId identificador del médico
     * @param fecha día a consultar
     * @return cantidad de turnos del médico en esa fecha
     */
    @Query("""
        SELECT COUNT(t) FROM Turno t
        WHERE t.medico.id = :medicoId
        AND t.fecha = :fecha
        """)
    Long contarTurnosPorMedicoYFecha(
        @Param("medicoId") Long medicoId,
        @Param("fecha") LocalDate fecha
    );

    /**
     * Verifica si existe un turno en un horario específico para un médico.
     *
     * @param medicoId identificador del médico
     * @param fecha fecha del turno
     * @param hora hora del turno
     * @return true si ya existe un turno en ese horario, false si está disponible
     */
    boolean existsByMedicoIdAndFechaAndHora(Long medicoId, LocalDate fecha, LocalDateTime hora);

    /**
     * Obtiene un turno por ID con sus relaciones eagerly cargadas (fetch join).
     * Evita LazyInitializationException al acceder a paciente y médico fuera de sesión.
     *
     * @param turnoId identificador del turno
     * @return Optional con el turno y sus relaciones cargadas
     */
    @Query("""
        SELECT DISTINCT t FROM Turno t
        LEFT JOIN FETCH t.paciente
        LEFT JOIN FETCH t.medico
        WHERE t.id = :turnoId
        """)
    Optional<Turno> findByIdWithRelations(@Param("turnoId") Long turnoId);

        @Query("SELECT t FROM Turno t WHERE t.hora BETWEEN :desde AND :hasta AND t.estado = :estado AND t.recordatorio24hEnviado = false")
    List<Turno> findTurnosParaRecordatorio24h(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        @Param("estado") EstadoTurno estado
    );

    @Query("SELECT t FROM Turno t WHERE t.hora BETWEEN :desde AND :hasta AND t.estado = :estado AND t.recordatorio2hEnviado = false")
    List<Turno> findTurnosParaRecordatorio2h(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        @Param("estado") EstadoTurno estado
    );

    @Query("SELECT t FROM Turno t WHERE t.hora < :limite AND t.estado = com.SGH.hospital.enums.EstadoTurno.PENDIENTE")
    List<Turno> findTurnosParaAusentes(
        @Param("limite") LocalDateTime limite
    );
}
