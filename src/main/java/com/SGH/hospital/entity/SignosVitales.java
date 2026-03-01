package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "signos_vitales")
public class SignosVitales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "presion_arterial")
    private String presionArterial; // Ej: "120/80"

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca; // ppm

    @Column(name = "temperatura")
    private Double temperatura; // °C

    @Column(name = "peso")
    private Double peso; // kg

    @Column(name = "altura")
    private Double altura; // metros

    @Column(name = "imc")
    private Double imc; // calculado automáticamente

    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno; // %

    // Constructor vacío
    public SignosVitales() {}

    /**
     * Calcula el IMC automáticamente al setear peso o altura.
     * IMC = peso / (altura * altura)
     */
    public void calcularIMC() {
        if (this.peso != null && this.altura != null && this.altura > 0) {
            this.imc = Math.round((this.peso / (this.altura * this.altura)) * 100.0) / 100.0;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) {
        this.peso = peso;
        calcularIMC();
    }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) {
        this.altura = altura;
        calcularIMC();
    }

    public Double getImc() { return imc; }

    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }
}