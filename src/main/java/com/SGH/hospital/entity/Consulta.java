package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "tratamiento")
    private String tratamiento;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "medico")
    private String medico;

    @Column(name = "signos_vitales")
    private String signosVitales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id")
    private Turno turno;

    ///////////////////////////////////////////
    //---------------Constructor---------------
    ///////////////////////////////////////////
    public Consulta(){} 
    
    public Consulta(String motivo, String diagnostico, String observaciones, String tratamiento, String fecha, String medico, String signosVitales, Turno turno) {
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.tratamiento = tratamiento;
        this.fecha = fecha;
        this.medico = medico;
        this.signosVitales = signosVitales;
        this.turno = turno;
    }

    //////////////////////////////////////////
    // ---------- Getters y Setters ----------
    //////////////////////////////////////////
    
    public Long getId() {
        return id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getSignosVitales() {
        return signosVitales;
    }

    public void setSignosVitales(String signosVitales) {
        this.signosVitales = signosVitales;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}
