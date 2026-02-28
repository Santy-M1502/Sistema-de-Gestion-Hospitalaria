package com.SGH.hospital.service;

import org.springframework.stereotype.Service;

import com.SGH.hospital.entity.Antecedente;
import com.SGH.hospital.exception.AntecedenteNotFoundException;
import com.SGH.hospital.repository.AntecedenteRepository;

@Service
public class AntecedenteService {
    
    private  final AntecedenteRepository antecedenteRepository;

    /**
     * Constructor para inyectar el repositorio de antecedentes.
     * @param antecedenteRepository
     */
    public AntecedenteService(AntecedenteRepository antecedenteRepository) {
        this.antecedenteRepository = antecedenteRepository;
    }

    /**
     * Busca un antecedente por su ID.
     * @param id
     * @return
     * @throws AntecedenteNotFoundException si no se encuentra el antecedente
     */
    public Antecedente buscarPorId(Long id) {
        return antecedenteRepository.findById(id)
            .orElseThrow(() -> new AntecedenteNotFoundException("Antecedente con id " + id + " no encontrado"));
    }

    /**
     * Elimina un antecedente por su ID.
     * @param id
     */
    public void eliminarAntecedente(Long id) {
        if (!antecedenteRepository.existsById(id)) {
            throw new AntecedenteNotFoundException("Antecedente con id " + id + " no encontrado");
        }
        antecedenteRepository.deleteById(id);
    }

    /**
     * Guarda un nuevo antecedente o actualiza uno existente.
     * @param antecedente
     * @return
     */
    public Antecedente guardarAntecedente(Antecedente antecedente) {
        if (antecedente == null) {
            throw new IllegalArgumentException("El antecedente no puede ser nulo");
        }
        return antecedenteRepository.save(antecedente);
    }
}
