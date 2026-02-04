package com.SGH.hospital.repository;

import org.springframework.stereotype.Repository;
import com.SGH.hospital.entity.ListaEspera;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {

    /**
     * Obtener todos los de un médico
     * 
     * @param medicoId
     * @return Medico
     */
    List<ListaEspera> findByMedicoId(Long medicoId);

    /**
     * Obtener todos de un paciente
     * 
     * @param pacienteId
     * @return Paciente
     */
    List<ListaEspera> findByPacienteId(Long pacienteId);

    /**
     * Obtener los de un día
     * 
     * @param fecha_solicitada
     * @return
     */
    Page<ListaEspera> findByFechaSolicitada(LocalDate fecha, Pageable pageable);

    /**
     * Obtener de un médico en un día
     * 
     * @param medicoId
     * @param fecha_solicitada
     * @return
     */
    List<ListaEspera> findByMedicoIdAndFechaSolicitada(Long medicoId, LocalDate fecha_solicitada);

    /**
     * Ordenados (el primero es el siguiente en recibir un turno)
     * 
     * @param medicoId
     * @return
     */
    List<ListaEspera> findByMedicoIdOrderByIdAsc(Long medicoId);

    /**
     * Obtener el próximo en la lista (para ofrecer turno cancelado)
     * 
     * @param medicoId
     * @return
     */
    ListaEspera findFirstByMedicoIdOrderByIdAsc(Long medicoId);

    /**
     * Verificar si hay alguien esperando para un médico
     * 
     * @param medicoId
     * @return
     */
    boolean existsByMedicoId(Long medicoId);

    /**
     * Ver si hay alguien esperando en un día puntual
     * 
     * @param medicoId
     * @param fechaSolicitada
     * @return
     */
    boolean existsByMedicoIdAndFechaSolicitada(Long medicoId, LocalDate fechaSolicitada);

    /**
     * Lista completa del día para todos los médicos
     * 
     * @param fecha
     * @return
     */
    @Query("SELECT l FROM ListaEspera l WHERE l.fechaSolicitada = :fecha ORDER BY l.id ASC")
    List<ListaEspera> findListaCompletaPorDia(@Param("fecha") LocalDate fecha);

    /**
     * Obtener la cantidad esperando por doctor
     * 
     * @param medicoId
     * @return
     */
    long countByMedicoId(Long medicoId);

    /**
     * Obtener cantidad de personas esperando un día
     * 
     * @param fecha_solicitada
     * @return
     */
    long countByFechaSolicitada(LocalDate fecha_solicitada);
}
