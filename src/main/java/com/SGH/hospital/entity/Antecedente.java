package com.SGH.hospital.entity;

import com.SGH.hospital.enums.TipoAntecedente;

import jakarta.persistence.*;

@Entity
@Table(name = "antecedente")
public class Antecedente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoAntecedente tipo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    private String fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_clinica_id", nullable = false) 
    private HistoriaClinica historiaClinica;

    public Antecedente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoAntecedente getTipo() { return tipo; }
    public void setTipo(TipoAntecedente tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public HistoriaClinica getHistoriaClinica() { return historiaClinica; }
    public void setHistoriaClinica(HistoriaClinica historiaClinica) { this.historiaClinica = historiaClinica; }
}