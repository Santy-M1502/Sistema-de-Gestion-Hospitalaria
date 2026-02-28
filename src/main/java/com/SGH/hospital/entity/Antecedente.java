package com.SGH.hospital.entity;

import com.SGH.hospital.enums.TipoAntecedente;

import jakarta.persistence.*;

@Entity
@Table(name = "antecedente")
public class Antecedente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private TipoAntecedente tipo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    private String fecha;

    ///////////////////////////////////////////
    //---------------Constructor---------------
    ///////////////////////////////////////////
    
    public Antecedente(){}

    public Antecedente(String descripcion, HistoriaClinica historiaClinica) {
        this.descripcion = descripcion;
    }

    //////////////////////////////////////////
    // ---------- Getters y Setters ----------
    //////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoAntecedente getTipo() {
        return tipo;
    }

    public void setTipo(TipoAntecedente tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
