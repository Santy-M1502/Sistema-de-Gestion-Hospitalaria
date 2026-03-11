package com.SGH.hospital.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.SGH.hospital.enums.EstadoReceta;

import jakarta.persistence.*;

@Entity
@Table(name = "receta")
public class Receta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReceta estadoReceta;

    @Column(name = "codigo_unico", nullable = false, unique = true, updatable = false)
    private String codigoUnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "fecha_dispensacion")
    private LocalDateTime fechaDispensacion;


    ///////////////////////////////////////////
    //---------------Constructor---------------
    ///////////////////////////////////////////
    
    public Receta(){}

    public Receta(LocalDate fechaEmision, LocalDate fechaVencimiento, Consulta consulta){
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.consulta = consulta;
    }

    //////////////////////////////////////////
    // ---------- Getters y Setters ----------
    //////////////////////////////////////////
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoReceta getEstadoReceta() {
        return estadoReceta;
    }

    public void setEstadoReceta(EstadoReceta estadoReceta) {
        this.estadoReceta = estadoReceta;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
}