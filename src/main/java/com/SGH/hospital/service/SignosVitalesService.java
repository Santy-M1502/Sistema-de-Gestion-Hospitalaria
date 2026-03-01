package com.SGH.hospital.service;

import com.SGH.hospital.entity.SignosVitales;
import com.SGH.hospital.exception.BadRequestException;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.repository.SignosVitalesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignosVitalesService {

    private final SignosVitalesRepository signosVitalesRepository;

    public SignosVitalesService(SignosVitalesRepository signosVitalesRepository) {
        this.signosVitalesRepository = signosVitalesRepository;
    }

    /**
     * Crea y guarda un registro de signos vitales.
     * El IMC se calcula automáticamente en la entidad al setear peso y altura.
     */
    public SignosVitales guardarSignosVitales(SignosVitales signosVitales) {
        if (signosVitales == null) {
            throw new BadRequestException("Los signos vitales no pueden ser nulos");
        }
        return signosVitalesRepository.save(signosVitales);
    }

    public SignosVitales buscarPorId(Long id) {
        return signosVitalesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Signos vitales con id " + id + " no encontrados"));
    }

    /**
     * Busca registros con temperatura mayor a la indicada.
     * Útil para detectar pacientes con fiebre (> 37.5°C).
     */
    public List<SignosVitales> buscarConFiebre(Double umbral) {
        return signosVitalesRepository.findByTemperaturaGreaterThan(umbral);
    }

    /**
     * Busca registros con saturación de oxígeno por debajo del umbral.
     * Útil para detectar hipoxia (< 95%).
     */
    public List<SignosVitales> buscarConHipoxia(Integer umbral) {
        return signosVitalesRepository.findBySaturacionOxigenoLessThan(umbral);
    }

    /**
     * Busca registros con frecuencia cardíaca fuera del rango normal (60-100 ppm).
     */
    public List<SignosVitales> buscarFrecuenciaAnormal() {
        return signosVitalesRepository.findFrecuenciaFueraDeRango(60, 100);
    }

    /**
     * Busca registros con sobrepeso u obesidad según IMC.
     * IMC > 25 sobrepeso, IMC > 30 obesidad.
     */
    public List<SignosVitales> buscarConSobrepeso() {
        return signosVitalesRepository.findByImcGreaterThan(25.0);
    }

    public List<SignosVitales> buscarPorRangoIMC(Double imcMin, Double imcMax) {
        return signosVitalesRepository.findByImcBetween(imcMin, imcMax);
    }

    public void eliminarSignosVitales(Long id) {
        if (!signosVitalesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Signos vitales con id " + id + " no encontrados");
        }
        signosVitalesRepository.deleteById(id);
    }
}