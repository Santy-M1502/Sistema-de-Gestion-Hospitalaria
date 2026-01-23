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
     * 
     * @param dni
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente por email
     * 
     * @param email
     */
    Optional<Paciente> findByEmail(String email);

    /**
     * Verifica si existe un paciente con el DNI especificado
     * 
     * @param dni
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe un paciente con el email especificado
     * 
     * @param email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un paciente con el DNI especificado, excluyendo un ID
     * Útil para validaciones en actualizaciones
     * 
     * @param dni
     * @param id
     */
    boolean existsByDniAndIdNot(String dni, Long id);

    /**
     * Verifica si existe un paciente con el email especificado, excluyendo un ID
     * 
     * @param email
     * @param id
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Busca pacientes por estado con paginación
     * 
     * @param estado
     * @param pageable
     */
    Page<Paciente> findByEstado(EstadoUsuario estado, Pageable pageable);

    /**
     * Busca pacientes cuyo nombre o apellido contenga el texto especificado
     * 
     * @param searchTerm
     * @param pageable
     */
    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Paciente> searchByNombreOrApellido(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Busca pacientes activos con paginación
     * 
     * @param pageable
     */
    @Query("SELECT p FROM Paciente p WHERE p.estado = 'ACTIVO'")
    Page<Paciente> findAllActivos(Pageable pageable);

    /**
     * Cuenta la cantidad de pacientes por estado
     * 
     * @param estado
     */
    long countByEstado(EstadoUsuario estado);
}