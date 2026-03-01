package com.SGH.hospital.entity;

import com.SGH.hospital.enums.GrupoSanguineo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo")
    private GrupoSanguineo grupoSanguineo;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "historia_clinica_id")
    private List<Antecedente> antecedentes;

    // Constructor vacío
    public HistoriaClinica() {}

    // Getters y Setters
    public Long getId() { return id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }

    public GrupoSanguineo getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public List<Antecedente> getAntecedentes() { return antecedentes; }
    public void setAntecedentes(List<Antecedente> antecedentes) { this.antecedentes = antecedentes; }
}