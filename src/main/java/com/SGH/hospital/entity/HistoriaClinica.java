package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "fecha_apertura")
    private String fechaApertura;

    @Column(name = "grupo_sanguineo")
    private String grupoSanguineo;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "antecedentes")
    private String antecedentes;

    ///////////////////////////////////////////
    //---------------Constructor---------------
    ///////////////////////////////////////////
    
    public HistoriaClinica(){}

    public HistoriaClinica(Paciente paciente, String fechaApertura, String grupoSanguineo, String alergias, String antecedentes) {
        this.paciente = paciente;
        this.fechaApertura = fechaApertura;
        this.grupoSanguineo = grupoSanguineo;
        this.alergias = alergias;
        this.antecedentes = antecedentes;
    }

    //////////////////////////////////////////
    // ---------- Getters y Setters ----------
    //////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }
}
