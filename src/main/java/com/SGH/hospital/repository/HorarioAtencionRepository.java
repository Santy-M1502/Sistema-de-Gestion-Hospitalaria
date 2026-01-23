package com.SGH.hospital.repository;

import com.SGH.hospital.entity.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    
    /**
     * Busca medico por Id
     * 
     * @param medicoId
     * @return
     */
    List<HorarioAtencion> findByMedicoId(Long medicoId);

    /**
     * Busca si el medico esta activo
     * 
     * @param medicoId
     * @param activo
     * @return
     */
    List<HorarioAtencion> findByMedicoIdAndActivo(Long medicoId, Boolean activo);
    
    /**
     * Busca los medicos activos cierta fecha
     * 
     * @param medicoId
     * @param dia
     * @return
     */
    @Query("SELECT h FROM HorarioAtencion h WHERE h.medico.id = :medicoId AND h.diaSemana = :dia AND h.activo = true")
    List<HorarioAtencion> findByMedicoAndDia(
        @Param("medicoId") Long medicoId, 
        @Param("dia") DayOfWeek dia
    );
    
    /**
     * Elimina / Desactiva medico
     * 
     * @param medicoId
     */
    void deleteByMedicoId(Long medicoId);
}