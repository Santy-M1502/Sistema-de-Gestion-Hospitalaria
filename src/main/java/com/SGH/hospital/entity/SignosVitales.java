package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "signos_vitales")
public class SignosVitales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "presion_arterial")
    private String presionArterial;

    @Column(name = "frecuencia_cardiaca")
    private String frecuenciaCardiaca;

    @Column(name = "temperatura")
    private String temperatura;

    @Column(name = "peso")
    private String peso;

    @Column(name = "altura")
    private String altura;
    
    @Column(name = "imc")
    private String imc;
    
    @Column(name = "saturacion_oxigeno")
    private String saturacion_oxigeno;
}
