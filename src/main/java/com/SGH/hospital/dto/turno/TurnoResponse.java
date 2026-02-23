package com.SGH.hospital.dto.turno;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.SGH.hospital.entity.Turno;

public class TurnoResponse {

    private Long id;
    private LocalDate fecha;
    private LocalDateTime hora;
    private String estado;
    private String motivoConsulta;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private Long medicoId;
    private String medicoNombre;
    private String medicoApellido;
    private String medicoMatricula;

    public TurnoResponse() {}

    public TurnoResponse(Turno turno) {
        this.id = turno.getId();
        this.fecha = turno.getFecha();
        this.hora = turno.getHora();
        this.estado = turno.getEstado() != null ? turno.getEstado().name() : null;
        this.motivoConsulta = turno.getMotivo();

        // Acceder solo a ID y campos básicos sin deserializar lazy proxies completos
        try {
            if (turno.getPaciente() != null) {
                this.pacienteId = turno.getPaciente().getId();
                this.pacienteNombre = turno.getPaciente().getNombre();
                this.pacienteApellido = turno.getPaciente().getApellido();
            }
        } catch (Exception e) {
            // Si hay error al acceder al paciente, al menos asignamos el ID
        }

        try {
            if (turno.getMedico() != null) {
                this.medicoId = turno.getMedico().getId();
                this.medicoNombre = turno.getMedico().getNombre();
                this.medicoApellido = turno.getMedico().getApellido();
                this.medicoMatricula = turno.getMedico().getMatricula();
            }
        } catch (Exception e) {
            // Si hay error al acceder al médico, al menos asignamos el ID
        }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalDateTime getHora() { return hora; }
    public void setHora(LocalDateTime hora) { this.hora = hora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getPacienteApellido() { return pacienteApellido; }
    public void setPacienteApellido(String pacienteApellido) { this.pacienteApellido = pacienteApellido; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    public String getMedicoApellido() { return medicoApellido; }
    public void setMedicoApellido(String medicoApellido) { this.medicoApellido = medicoApellido; }
    public String getMedicoMatricula() { return medicoMatricula; }
    public void setMedicoMatricula(String medicoMatricula) { this.medicoMatricula = medicoMatricula; }
}
