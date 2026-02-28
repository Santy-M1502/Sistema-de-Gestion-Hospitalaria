package com.SGH.hospital.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Diagnostico {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Cie10 cie10;

    @ManyToOne
    private Turno turno;

    private String observaciones;

    private LocalDateTime fecha;


    //////////////////////////////////////////////////////////
    // ==================== Constructores ====================
    //////////////////////////////////////////////////////////

    public Diagnostico() {}

    public Diagnostico(Cie10 cie10, Turno turno, String observaciones) {
        this.cie10 = cie10;
        this.turno = turno;
        this.observaciones = observaciones;
        this.fecha = LocalDateTime.now();
    }

    //////////////////////////////////////////////////////////
    /// ==================== Getters y Setters ====================
    //////////////////////////////////////////////////////////
    
    public Long getId() {
        return id;
    }

    public Cie10 getCie10() {
        return cie10;
    }

    public void setCie10(Cie10 cie10) {
        this.cie10 = cie10;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
