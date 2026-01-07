package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca un paciente por DNI
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente por email
     */
    Optional<Paciente> findByEmail(String email);

    /**
     * Verifica si existe un paciente con el DNI especificado
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe un paciente con el email especificado
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un paciente con el DNI especificado, excluyendo un ID
     * Útil para validaciones en actualizaciones
     */
    boolean existsByDniAndIdNot(String dni, Long id);

    /**
     * Verifica si existe un paciente con el email especificado, excluyendo un ID
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Busca pacientes por estado con paginación
     */
    Page<Paciente> findByEstado(EstadoUsuario estado, Pageable pageable);

    /**
     * Busca pacientes cuyo nombre o apellido contenga el texto especificado
     */
    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Paciente> searchByNombreOrApellido(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Busca pacientes activos con paginación
     */
    @Query("SELECT p FROM Paciente p WHERE p.estado = 'ACTIVO'")
    Page<Paciente> findAllActivos(Pageable pageable);

    /**
     * Cuenta la cantidad de pacientes por estado
     */
    long countByEstado(EstadoUsuario estado);
}