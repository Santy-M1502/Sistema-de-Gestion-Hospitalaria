package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Medico;
import com.SGH.hospital.enums.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // Búsquedas básicas
    Optional<Medico> findByMatricula(String matricula);
    Optional<Medico> findByEmail(String email);
    Optional<Medico> findByDni(String dni);
    
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    // Búsqueda por estado
    Page<Medico> findByEstado(EstadoUsuario estado, Pageable pageable);
    List<Medico> findByEstadoAndDisponible(EstadoUsuario estado, Boolean disponible);

    // Búsqueda por disponibilidad
    Page<Medico> findByDisponible(Boolean disponible, Pageable pageable);

    // Búsqueda por especialidad
    @Query("SELECT DISTINCT m FROM Medico m JOIN m.especialidades e WHERE e.id = :especialidadId")
    Page<Medico> findByEspecialidadId(@Param("especialidadId") Long especialidadId, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Medico m JOIN m.especialidades e WHERE e.nombre = :nombreEspecialidad")
    List<Medico> findByEspecialidadNombre(@Param("nombreEspecialidad") String nombreEspecialidad);

    // Búsqueda combinada: especialidad + disponibilidad + estado
    @Query("SELECT DISTINCT m FROM Medico m JOIN m.especialidades e " +
           "WHERE (:especialidadId IS NULL OR e.id = :especialidadId) " +
           "AND (:disponible IS NULL OR m.disponible = :disponible) " +
           "AND (:estado IS NULL OR m.estado = :estado)")
    Page<Medico> findByEspecialidadAndDisponibilidad(
        @Param("especialidadId") Long especialidadId,
        @Param("disponible") Boolean disponible,
        @Param("estado") EstadoUsuario estado,
        Pageable pageable
    );

    // Búsqueda avanzada con múltiples filtros
    @Query("SELECT DISTINCT m FROM Medico m LEFT JOIN m.especialidades e " +
           "WHERE (:nombre IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:apellido IS NULL OR LOWER(m.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) " +
           "AND (:especialidadId IS NULL OR e.id = :especialidadId) " +
           "AND (:disponible IS NULL OR m.disponible = :disponible) " +
           "AND (:estado IS NULL OR m.estado = :estado)")
    Page<Medico> buscarConFiltros(
        @Param("nombre") String nombre,
        @Param("apellido") String apellido,
        @Param("especialidadId") Long especialidadId,
        @Param("disponible") Boolean disponible,
        @Param("estado") EstadoUsuario estado,
        Pageable pageable
    );

    // Obtener médico con especialidades (fetch join para evitar N+1)
    @Query("SELECT DISTINCT m FROM Medico m LEFT JOIN FETCH m.especialidades WHERE m.id = :id")
    Optional<Medico> findByIdWithEspecialidades(@Param("id") Long id);

    // Obtener médico con horarios
    @Query("SELECT DISTINCT m FROM Medico m LEFT JOIN FETCH m.horariosAtencion WHERE m.id = :id")
    Optional<Medico> findByIdWithHorarios(@Param("id") Long id);

    // Obtener médico con todo (especialidades + horarios)
    @Query("SELECT DISTINCT m FROM Medico m " +
           "LEFT JOIN FETCH m.especialidades " +
           "LEFT JOIN FETCH m.horariosAtencion " +
           "WHERE m.id = :id")
    Optional<Medico> findByIdWithAll(@Param("id") Long id);

    // Contar médicos por especialidad
    @Query("SELECT COUNT(DISTINCT m) FROM Medico m JOIN m.especialidades e WHERE e.id = :especialidadId")
    Long countByEspecialidadId(@Param("especialidadId") Long especialidadId);
}
