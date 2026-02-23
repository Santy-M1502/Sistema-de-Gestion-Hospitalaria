package com.SGH.hospital.dto.turno;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.SGH.hospital.enums.EstadoTurno;

public class TurnoDTO {

    private Long id;
    private LocalDate fecha;
    private LocalDateTime hora;
    private EstadoTurno estado;
    private String motivoConsulta;

    // DATOS PACIENTE (no entidad)
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private String pacienteEmail;
    private String pacienteDireccion;

    // DATOS MEDICO (no entidad)
    private Long medicoId;
    private String medicoNombre;
    private String medicoApellido;
    private String medicoEspecialidades;

    private boolean recordatorio24hEnviado;
    private boolean recordatorio2hEnviado;

    public TurnoDTO(){}

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public LocalDate getFecha(){ return fecha; }
    public void setFecha(LocalDate fecha){ this.fecha = fecha; }

    public LocalDateTime getHora(){ return hora; }
    public void setHora(LocalDateTime hora){ this.hora = hora; }

    public EstadoTurno getEstado(){ return estado; }
    public void setEstado(EstadoTurno estado){ this.estado = estado; }

    public String getMotivoConsulta(){ return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta){ this.motivoConsulta = motivoConsulta; }

    public Long getPacienteId(){ return pacienteId; }
    public void setPacienteId(Long pacienteId){ this.pacienteId = pacienteId; }

    public String getPacienteNombre(){ return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre){ this.pacienteNombre = pacienteNombre; }

    public String getPacienteApellido(){ return pacienteApellido; }
    public void setPacienteApellido(String pacienteApellido){ this.pacienteApellido = pacienteApellido; }

    public String getPacienteEmail(){ return pacienteEmail; }
    public void setPacienteEmail(String pacienteEmail){ this.pacienteEmail = pacienteEmail; }

    public Long getMedicoId(){ return medicoId; }
    public void setMedicoId(Long medicoId){ this.medicoId = medicoId; }

    public String getMedicoNombre(){ return medicoNombre; }
    public void setMedicoNombre(String medicoNombre){ this.medicoNombre = medicoNombre; }

    public String getMedicoApellido(){ return medicoApellido; }
    public void setMedicoApellido(String medicoApellido){ this.medicoApellido = medicoApellido; }

    public boolean isRecordatorio24hEnviado(){ return recordatorio24hEnviado; }
    public void setRecordatorio24hEnviado(boolean v){ this.recordatorio24hEnviado = v; }

    public boolean isRecordatorio2hEnviado(){ return recordatorio2hEnviado; }
    public void setRecordatorio2hEnviado(boolean v){ this.recordatorio2hEnviado = v; }

    public String getPacienteDireccion() { return pacienteDireccion; }
    public void setPacienteDireccion(String pacienteDireccion) { this.pacienteDireccion = pacienteDireccion; }

    public String getMedicoEspecialidades() { return medicoEspecialidades; }
    public void setMedicoEspecialidades(String medicoEspecialidades) { this.medicoEspecialidades = medicoEspecialidades; }
}