package com.SGH.hospital.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lista_espera")
public class ListaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private LocalDate fecha_solicitada;

    ////////////////////////////////////////////////////////
    //================== Constructor========================
    ////////////////////////////////////////////////////////
    public ListaEspera() {}

    public ListaEspera(Paciente paciente, Medico medico, LocalDate fechaSolicitda){
        this.paciente = paciente;
        this.medico = medico;
        this.fecha_solicitada = fechaSolicitda;
    }

    //////////////////////////////////////////////////////////
    //===================Getter y Setter======================
    //////////////////////////////////////////////////////////

    public Long getId(){
        return id;
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

    public LocalDate getFechaSolicitada(){
        return fecha_solicitada;
    }

    public void setFechaSolicitada(LocalDate fecha_solicitada){
        this.fecha_solicitada = fecha_solicitada;
    }
}
