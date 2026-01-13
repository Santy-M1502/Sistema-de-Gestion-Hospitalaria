package com.SGH.hospital.service;

import com.SGH.hospital.entity.Especialidad;
import com.SGH.hospital.exception.EspecialidadNotFoundException;
import com.SGH.hospital.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository repository;

    public EspecialidadService(EspecialidadRepository repository) {
        this.repository = repository;
    }

    public Especialidad crear(Especialidad esp) {
        return repository.save(esp);
    }

    public List<Especialidad> listar() {
        return repository.findAll();
    }

    public Especialidad obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + id));
    }

    public Especialidad actualizar(Long id, Especialidad esp) {
        Especialidad actual = obtener(id);
        actual.setNombre(esp.getNombre());
        actual.setActiva(esp.getActiva());
        return repository.save(actual);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id))
            throw new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + id);
        repository.deleteById(id);
    }
}
