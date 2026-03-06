package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "signos_vitales")
public class SignosVitales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "presion_arterial", nullable = false)
    private String presionArterial;

    @Column(name = "frecuencia_cardiaca", nullable = false)
    private Integer frecuenciaCardiaca;

    @Column(name = "temperatura", nullable = false)
    private Double temperatura;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "altura", nullable = false)
    private Double altura;

    @Column(name = "imc")
    private Double imc;

    @Column(name = "saturacion_oxigeno", nullable = false)
    private Integer saturacionOxigeno;

    // IMC se calcula automáticamente al setear peso y altura
    @PostLoad
    @PrePersist
    @PreUpdate
    public void calcularImc() {
        if (peso != null && altura != null && altura > 0) {
            this.imc = Math.round((peso / (altura * altura)) * 100.0) / 100.0;
        }
    }

    public SignosVitales() {}

    public Long getId() { return id; }

    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getImc() { return imc; }

    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }
}