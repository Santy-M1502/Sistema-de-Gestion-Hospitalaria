package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
    Optional<Especialidad> findByNombre(String nombre);
    List<Especialidad> findByActiva(Boolean activa);
    boolean existsByNombre(String nombre);
    
    @Query("SELECT e FROM Especialidad e WHERE e.activa = true ORDER BY e.nombre")
    List<Especialidad> findAllActivas();
}