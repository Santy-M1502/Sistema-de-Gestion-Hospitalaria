package com.SGH.hospital.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.SGH.hospital.enums.EstadoTurno;

import jakarta.persistence.*;

@Entity
@Table(name = "turno")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalDateTime hora;

    @Column(nullable = false)
    private EstadoTurno estado;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    ///////////////////////////////////////////
    //---------------Constructor---------------
    ///////////////////////////////////////////
    public Turno(){}

    public Turno(String motivo, Paciente paciente, Medico medico){
        this.estado = EstadoTurno.PENDIENTE;
        this.fecha = LocalDate.now();
        this.hora = LocalDateTime.now();
        this.motivoConsulta = motivo;
        this.paciente = paciente;
        this.medico = medico;
    }

    //////////////////////////////////////////
    // ---------- Getters y Setters ----------
    //////////////////////////////////////////

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDate getFecha(){
        return fecha;
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
