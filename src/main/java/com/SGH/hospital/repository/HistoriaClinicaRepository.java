package com.SGH.hospital.repository;

import com.SGH.hospital.entity.HistoriaClinica;
import com.SGH.hospital.enums.GrupoSanguineo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    // Busca la historia clínica de un paciente específico
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);

    // Verifica si un paciente ya tiene historia clínica
    boolean existsByPacienteId(Long pacienteId);

    // Busca todas las historias clínicas de un grupo sanguíneo
    List<HistoriaClinica> findByGrupoSanguineo(GrupoSanguineo grupoSanguineo);

    // Busca historias clínicas que contengan cierta alergia
    List<HistoriaClinica> findByAlergiasContainingIgnoreCase(String alergia);

    // Trae historia clínica con todas sus relaciones cargadas
    @Query("SELECT hc FROM HistoriaClinica hc " +
           "LEFT JOIN FETCH hc.paciente " +
           "LEFT JOIN FETCH hc.antecedentes " +
           "WHERE hc.id = :id")
    Optional<HistoriaClinica> findByIdWithRelations(@Param("id") Long id);

    // Trae historia clínica del paciente con todas sus relaciones
    @Query("SELECT hc FROM HistoriaClinica hc " +
           "LEFT JOIN FETCH hc.paciente " +
           "LEFT JOIN FETCH hc.antecedentes " +
           "WHERE hc.paciente.id = :pacienteId")
    Optional<HistoriaClinica> findByPacienteIdWithRelations(@Param("pacienteId") Long pacienteId);
}