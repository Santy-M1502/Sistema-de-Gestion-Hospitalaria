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

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {


    List<Turno> findByPacienteId(Long pacienteId);
    
    List<Turno> findByMedicoId(Long medicoId);

    List<Turno> findByFecha(LocalDate fecha);

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

    List<Turno> findByEstado(EstadoTurno estado);

    @Query("""
        SELECT COUNT(t) FROM Turno t
        WHERE t.medico.id = :medicoId
        AND t.fecha = :fecha
        """)
        Long contarTurnosPorMedicoYFecha(
            @Param("medicoId") Long medicoId,
            @Param("fecha") LocalDate fecha
        );

    boolean existsByMedicoIdAndFechaAndHora(Long medicoId, LocalDate fecha, LocalDateTime hora);
}
