package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Busca todas las consultas de un médico
    List<Consulta> findByMedicoId(Long medicoId);

    // Busca todas las consultas asociadas a un turno
    Optional<Consulta> findByTurnoId(Long turnoId);

    // Verifica si ya existe una consulta para un turno
    boolean existsByTurnoId(Long turnoId);

    // Busca consultas de un médico en un rango de fechas
    List<Consulta> findByMedicoIdAndFechaBetween(Long medicoId, LocalDateTime desde, LocalDateTime hasta);

    // Busca consultas por diagnóstico (búsqueda parcial)
    List<Consulta> findByDiagnosticoContainingIgnoreCase(String diagnostico);

    // Busca consultas de un paciente a través del turno
    @Query("SELECT c FROM Consulta c " +
           "JOIN c.turno t " +
           "WHERE t.paciente.id = :pacienteId " +
           "ORDER BY c.fecha DESC")
    List<Consulta> findByPacienteId(@Param("pacienteId") Long pacienteId);

    // Trae consulta con todas sus relaciones cargadas
    @Query("SELECT c FROM Consulta c " +
           "LEFT JOIN FETCH c.turno " +
           "LEFT JOIN FETCH c.medico " +
           "LEFT JOIN FETCH c.signosVitales " +
           "WHERE c.id = :id")
    Optional<Consulta> findByIdWithRelations(@Param("id") Long id);

    // Busca consultas en un rango de fechas
    List<Consulta> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    // Cuenta consultas de un médico en un rango de fechas
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.medico.id = :medicoId AND c.fecha BETWEEN :desde AND :hasta")
    Long contarConsultasDeMedicoEnPeriodo(
            @Param("medicoId") Long medicoId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}