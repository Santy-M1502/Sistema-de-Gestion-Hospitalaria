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
    
    List<HorarioAtencion> findByMedicoId(Long medicoId);
    List<HorarioAtencion> findByMedicoIdAndActivo(Long medicoId, Boolean activo);
    
    @Query("SELECT h FROM HorarioAtencion h WHERE h.medico.id = :medicoId AND h.diaSemana = :dia AND h.activo = true")
    List<HorarioAtencion> findByMedicoAndDia(
        @Param("medicoId") Long medicoId, 
        @Param("dia") DayOfWeek dia
    );
    
    void deleteByMedicoId(Long medicoId);
}