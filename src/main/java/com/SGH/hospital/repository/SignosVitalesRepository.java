package com.SGH.hospital.repository;

import com.SGH.hospital.entity.SignosVitales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignosVitalesRepository extends JpaRepository<SignosVitales, Long> {

    // Busca registros con temperatura mayor a la indicada (fiebre, etc.)
    List<SignosVitales> findByTemperaturaGreaterThan(Double temperatura);

    // Busca registros con saturación menor a la indicada (hipoxia, etc.)
    List<SignosVitales> findBySaturacionOxigenoLessThan(Integer saturacion);

    // Busca registros con frecuencia cardíaca fuera del rango normal
    @Query("SELECT s FROM SignosVitales s WHERE s.frecuenciaCardiaca < :min OR s.frecuenciaCardiaca > :max")
    List<SignosVitales> findFrecuenciaFueraDeRango(@Param("min") Integer min, @Param("max") Integer max);

    // Busca registros con IMC mayor al indicado
    List<SignosVitales> findByImcGreaterThan(Double imc);

    // Busca registros con IMC entre dos valores
    List<SignosVitales> findByImcBetween(Double imcMin, Double imcMax);
}