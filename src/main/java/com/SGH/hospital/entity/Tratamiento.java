package com.SGH.hospital.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tratamiento")
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tratamiento_consulta",
        joinColumns = @JoinColumn(name = "tratamiento_id"),
        inverseJoinColumns = @JoinColumn(name = "consulta_id")
    )
    private List<Consulta> consultas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "diagnostico_tratamiento",
        joinColumns = @JoinColumn(name = "tratamiento_id"),
        inverseJoinColumns = @JoinColumn(name = "diagnostico_id")
    )
    private List<Diagnostico> diagnosticos;

    ///////////////////////////////////////////
    /// Constructor
    ///////////////////////////////////////////
    
    public Tratamiento(){}

    public Tratamiento(List<Consulta> consultas, List<Diagnostico> diagnosticos) {
        this.consultas = consultas;
        this.diagnosticos = diagnosticos;
    }

    //////////////////////////////////////////
    /// Getters y Setters
    ////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public List<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }
}
