package com.SGH.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SGH.hospital.entity.Antecedente;
import com.SGH.hospital.enums.TipoAntecedente;

@Repository
public interface AntecedenteRepository extends JpaRepository<Antecedente, Long> {

    // -----------------------------------------------
    // Búsquedas por tipo
    // -----------------------------------------------

    /**
     * Busca todos los antecedentes de un tipo específico.
     * Ejemplo: todos los antecedentes QUIRURGICO
     */
    List<Antecedente> findByTipo(TipoAntecedente tipo);

    /**
     * Verifica si existe algún antecedente de un tipo específico.
     */
    boolean existsByTipo(TipoAntecedente tipo);

    /**
     * Cuenta cuántos antecedentes hay de un tipo específico.
     */
    Long countByTipo(TipoAntecedente tipo);

    // -----------------------------------------------
    // Búsquedas por descripción
    // -----------------------------------------------

    /**
     * Busca antecedentes cuya descripción contenga el texto dado.
     * No distingue mayúsculas/minúsculas.
     */
    List<Antecedente> findByDescripcionContainingIgnoreCase(String descripcion);

    // -----------------------------------------------
    // Búsquedas por fecha
    // -----------------------------------------------

    /**
     * Busca todos los antecedentes registrados en una fecha específica.
     */
    List<Antecedente> findByFecha(String fecha);

    /**
     * Busca antecedentes de un tipo específico en una fecha específica.
     */
    List<Antecedente> findByTipoAndFecha(TipoAntecedente tipo, String fecha);

    // -----------------------------------------------
    // Queries personalizadas
    // -----------------------------------------------

    /**
     * Busca antecedentes cuya descripción contenga una palabra clave,
     * filtrado además por tipo.
     */
    @Query("SELECT a FROM Antecedente a WHERE a.tipo = :tipo AND LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))")
    List<Antecedente> buscarPorTipoYDescripcion(
            @Param("tipo") TipoAntecedente tipo,
            @Param("descripcion") String descripcion);

    /**
     * Obtiene todos los antecedentes ordenados por fecha descendente.
     */
    @Query("SELECT a FROM Antecedente a ORDER BY a.fecha DESC")
    List<Antecedente> findAllOrdenadosPorFechaDesc();

    /**
     * Cuenta el total de antecedentes agrupados por tipo.
     * Devuelve una lista de pares [TipoAntecedente, cantidad].
     */
    @Query("SELECT a.tipo, COUNT(a) FROM Antecedente a GROUP BY a.tipo")
    List<Object[]> contarAntecedentesAgrupadosPorTipo();
}