package com.SGH.hospital.repository;

import com.SGH.hospital.entity.SignosVitales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignosVitalesRepository extends JpaRepository<SignosVitales, Long> {

    List<SignosVitales> findByTemperaturaGreaterThan(Double umbral);

    List<SignosVitales> findBySaturacionOxigenoLessThan(Integer umbral);

    List<SignosVitales> findByImcGreaterThan(Double imc);

    List<SignosVitales> findByImcBetween(Double imcMin, Double imcMax);

    @Query("SELECT s FROM SignosVitales s WHERE s.frecuenciaCardiaca < :min OR s.frecuenciaCardiaca > :max")
    List<SignosVitales> findFrecuenciaFueraDeRango(@Param("min") Integer min, @Param("max") Integer max);
}