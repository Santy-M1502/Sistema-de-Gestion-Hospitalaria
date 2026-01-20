package com.SGH.hospital.dto.turno;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.SGH.hospital.entity.Medico;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.EstadoTurno;

public class TurnoDTO {

    private Long id;
    private LocalDate fecha;
    private LocalDateTime hora;
    private EstadoTurno estado;
    private String motivoConsulta;
    private Paciente paciente;
    private Medico medico;

    public TurnoDTO(){}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDate getFecha(){
        return this.fecha;
    }

    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }

    public LocalDateTime getHora(){
        return hora;
    }

    public void setHora(LocalDateTime hora){
        this.hora = hora;
    }

    public EstadoTurno getEstado(){
        return estado;
    }

    public void setEstado(EstadoTurno estado){
        this.estado = estado;
    }

    public String getMotivo(){
        return motivoConsulta;
    }

    public void setMotivo(String motivoConsulta){
        this.motivoConsulta = motivoConsulta;
    }

    public Paciente getPaciente(){
        return paciente;
    }

    public void setPaciente(Paciente paciente){
        this.paciente = paciente;
    }

    public Medico getMedico(){
        return medico;
    }

    public void setMedico(Medico medico){
        this.medico = medico;
    }

}
