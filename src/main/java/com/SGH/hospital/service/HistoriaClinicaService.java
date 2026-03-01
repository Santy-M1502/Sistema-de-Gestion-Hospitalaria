package com.SGH.hospital.service;

import com.SGH.hospital.entity.HistoriaClinica;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.GrupoSanguineo;
import com.SGH.hospital.exception.BadRequestException;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.repository.HistoriaClinicaRepository;
import com.SGH.hospital.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository,
                                   PacienteRepository pacienteRepository) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Crea una historia clínica para un paciente.
     * Valida que el paciente exista y que no tenga una historia clínica ya creada.
     */
    public HistoriaClinica crearHistoriaClinica(Long pacienteId, GrupoSanguineo grupoSanguineo, String alergias) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente con id " + pacienteId + " no encontrado"));

        if (historiaClinicaRepository.existsByPacienteId(pacienteId)) {
            throw new BadRequestException("El paciente ya tiene una historia clínica registrada");
        }

        HistoriaClinica hc = new HistoriaClinica();
        hc.setPaciente(paciente);
        hc.setFechaApertura(LocalDate.now());
        hc.setGrupoSanguineo(grupoSanguineo);
        hc.setAlergias(alergias);

        return historiaClinicaRepository.save(hc);
    }

    public HistoriaClinica buscarPorId(Long id) {
        return historiaClinicaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historia clínica con id " + id + " no encontrada"));
    }

    public HistoriaClinica buscarPorPacienteId(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteIdWithRelations(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("El paciente con id " + pacienteId + " no tiene historia clínica"));
    }

    public List<HistoriaClinica> buscarPorGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        return historiaClinicaRepository.findByGrupoSanguineo(grupoSanguineo);
    }

    public List<HistoriaClinica> buscarPorAlergia(String alergia) {
        return historiaClinicaRepository.findByAlergiasContainingIgnoreCase(alergia);
    }

    public HistoriaClinica actualizarAlergias(Long id, String alergias) {
        HistoriaClinica hc = buscarPorId(id);
        hc.setAlergias(alergias);
        return historiaClinicaRepository.save(hc);
    }

    public HistoriaClinica actualizarGrupoSanguineo(Long id, GrupoSanguineo grupoSanguineo) {
        HistoriaClinica hc = buscarPorId(id);
        hc.setGrupoSanguineo(grupoSanguineo);
        return historiaClinicaRepository.save(hc);
    }

    public void eliminarHistoriaClinica(Long id) {
        if (!historiaClinicaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historia clínica con id " + id + " no encontrada");
        }
        historiaClinicaRepository.deleteById(id);
    }
}