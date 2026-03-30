package com.SGH.hospital.service;

import com.SGH.hospital.dto.especialidad.EspecialidadRequest;
import com.SGH.hospital.dto.especialidad.EspecialidadResponse;
import com.SGH.hospital.entity.Especialidad;
import com.SGH.hospital.exception.EspecialidadNotFoundException;
import com.SGH.hospital.repository.EspecialidadRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class EspecialidadService {

    private final EspecialidadRepository repository;

    public EspecialidadService(EspecialidadRepository repository) {
        this.repository = repository;
    }

    public EspecialidadResponse crear(EspecialidadRequest request) {
        Especialidad esp = new Especialidad(request.getNombre());
        esp.setActiva(request.getActiva() != null ? request.getActiva() : true);
        return toResponse(repository.save(esp));
    }

    public List<EspecialidadResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EspecialidadResponse obtener(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    public EspecialidadResponse actualizar(Long id, EspecialidadRequest request) {
        Especialidad actual = obtenerEntidad(id);
        actual.setNombre(request.getNombre());
        if (request.getActiva() != null) actual.setActiva(request.getActiva());
        return toResponse(repository.save(actual));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id))
            throw new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + id);
        repository.deleteById(id);
    }

    private Especialidad obtenerEntidad(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + id));
    }

    private EspecialidadResponse toResponse(Especialidad esp) {
        EspecialidadResponse response = new EspecialidadResponse(esp.getId(), esp.getNombre(), esp.getActiva());

        if (esp.getMedicos() != null) {
            List<EspecialidadResponse.MedicoResumen> medicos = esp.getMedicos().stream()
                    .map(m -> new EspecialidadResponse.MedicoResumen(
                            m.getId(), m.getNombre(), m.getApellido(), m.getMatricula()))
                    .collect(Collectors.toList());
            response.setMedicos(medicos);
        }

        return response;
    }
}