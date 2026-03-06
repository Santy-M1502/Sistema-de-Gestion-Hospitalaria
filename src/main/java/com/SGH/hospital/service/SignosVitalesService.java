package com.SGH.hospital.service;

import com.SGH.hospital.dto.signosVitales.signosVitalesRequest;
import com.SGH.hospital.dto.signosVitales.signosVitalesResponse;
import com.SGH.hospital.entity.SignosVitales;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.mapper.SignosVitalesMapper;
import com.SGH.hospital.repository.SignosVitalesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignosVitalesService {

    private final SignosVitalesRepository signosVitalesRepository;
    private final SignosVitalesMapper signosVitalesMapper;

    public SignosVitalesService(SignosVitalesRepository signosVitalesRepository,
                                SignosVitalesMapper signosVitalesMapper) {
        this.signosVitalesRepository = signosVitalesRepository;
        this.signosVitalesMapper = signosVitalesMapper;
    }

    public signosVitalesResponse guardar(signosVitalesRequest request) {
        SignosVitales entidad = signosVitalesMapper.toEntity(request);
        return signosVitalesMapper.toResponse(signosVitalesRepository.save(entidad));
    }

    public signosVitalesResponse buscarPorId(Long id) {
        return signosVitalesMapper.toResponse(
                signosVitalesRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Signos vitales con id " + id + " no encontrados")));
    }

    public List<signosVitalesResponse> buscarConFiebre(Double umbral) {
        return signosVitalesRepository.findByTemperaturaGreaterThan(umbral)
                .stream().map(signosVitalesMapper::toResponse).toList();
    }

    public List<signosVitalesResponse> buscarConHipoxia(Integer umbral) {
        return signosVitalesRepository.findBySaturacionOxigenoLessThan(umbral)
                .stream().map(signosVitalesMapper::toResponse).toList();
    }

    public List<signosVitalesResponse> buscarFrecuenciaAnormal() {
        return signosVitalesRepository.findFrecuenciaFueraDeRango(60, 100)
                .stream().map(signosVitalesMapper::toResponse).toList();
    }

    public List<signosVitalesResponse> buscarConSobrepeso() {
        return signosVitalesRepository.findByImcGreaterThan(25.0)
                .stream().map(signosVitalesMapper::toResponse).toList();
    }

    public List<signosVitalesResponse> buscarPorRangoIMC(Double imcMin, Double imcMax) {
        return signosVitalesRepository.findByImcBetween(imcMin, imcMax)
                .stream().map(signosVitalesMapper::toResponse).toList();
    }

    public void eliminar(Long id) {
        if (!signosVitalesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Signos vitales con id " + id + " no encontrados");
        }
        signosVitalesRepository.deleteById(id);
    }
}