package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
    // ==================== Búsquedas básicas ====================
    
    /**
     * Busca una especialidad por nombre (exacto)
     */
    Optional<Especialidad> findByNombre(String nombre);
    
    /**
     * Busca especialidades por estado activo/inactivo
     */
    List<Especialidad> findByActiva(Boolean activa);
    
    /**
     * Verifica si existe una especialidad con ese nombre
     */
    boolean existsByNombre(String nombre);
    
    // ==================== Queries personalizadas ====================
    
    /**
     * Obtiene todas las especialidades activas ordenadas alfabéticamente
     */
    @Query("SELECT e FROM Especialidad e WHERE e.activa = true ORDER BY e.nombre")
    List<Especialidad> findAllActivas();
    
    /**
     * Busca especialidades activas por nombre (búsqueda parcial)
     */
    @Query("SELECT e FROM Especialidad e WHERE e.activa = true " +
           "AND LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "ORDER BY e.nombre")
    List<Especialidad> buscarActivasPorNombre(@Param("nombre") String nombre);
    
    /**
     * Obtiene especialidades con al menos un médico asignado
     */
    @Query("SELECT DISTINCT e FROM Especialidad e " +
           "JOIN e.medicos m " +
           "WHERE e.activa = true " +
           "ORDER BY e.nombre")
    List<Especialidad> findEspecialidadesConMedicos();
    
    /**
     * Cuenta cuántos médicos tienen esta especialidad
     */
    @Query("SELECT COUNT(m) FROM Especialidad e " +
           "JOIN e.medicos m " +
           "WHERE e.id = :especialidadId AND m.disponible = true")
    Long contarMedicosDisponibles(@Param("especialidadId") Long especialidadId);
}