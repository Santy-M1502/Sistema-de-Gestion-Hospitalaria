package com.SGH.hospital.dto.listaEspera;

import java.time.LocalDate;

import com.SGH.hospital.dto.medico.MedicoResponse;
import com.SGH.hospital.dto.paciente.PacienteResponse;
import com.SGH.hospital.entity.ListaEspera;
import com.SGH.hospital.entity.Medico;

public class ListaEsperaResponse {

    private Long id;
    private PacienteResponse paciente;
    private MedicoResponse medico;
    private LocalDate fechaSolicitada;

    public ListaEsperaResponse(ListaEspera listaEspera) {
        this.id = listaEspera.getId();
        // Convertir paciente a DTO (safe — no colecciones perezosas)
        this.paciente = PacienteResponse.fromEntity(listaEspera.getPaciente());

        // Crear un MedicoResponse superficial para evitar inicializar colecciones lazy
        Medico m = listaEspera.getMedico();
        MedicoResponse mr = new MedicoResponse();
        mr.setId(m.getId());
        mr.setNombre(m.getNombre());
        mr.setApellido(m.getApellido());
        mr.setDni(m.getDni());
        mr.setEmail(m.getEmail());
        mr.setTelefono(m.getTelefono());
        mr.setDireccion(m.getDireccion());
        mr.setMatricula(m.getMatricula());
        mr.setAniosExperiencia(m.getAniosExperiencia());
        mr.setBiografia(m.getBiografia());
        mr.setDisponible(m.getDisponible());
        mr.setEstado(m.getEstado());
        // No setear especialidades ni horarios para evitar LazyInitializationException
        this.medico = mr;

        this.fechaSolicitada = listaEspera.getFechaSolicitada();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PacienteResponse getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponse paciente) {
        this.paciente = paciente;
    }

    public MedicoResponse getMedico() {
        return medico;
    }

    public void setMedico(MedicoResponse medico) {
        this.medico = medico;
    }

    public LocalDate getFechaSolicitada() {
        return fechaSolicitada;
    }

    public void setFechaSolicitada(LocalDate fechaSolicitada) {
        this.fechaSolicitada = fechaSolicitada;
    }
}
