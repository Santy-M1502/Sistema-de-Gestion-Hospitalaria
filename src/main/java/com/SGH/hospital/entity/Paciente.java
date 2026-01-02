package com.SGH.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    private String obraSocial;
    
    @Column(nullable = false)
    private String numeroAfiliado;

    public String getObraSocial(){
        return this.obraSocial;
    }

    public void setObraSocial(String obraSocial){
        this.obraSocial = obraSocial;
    }

    public String getNumeroAfiliado(){
        return this.numeroAfiliado;
    }

    public void setNumeroAfiliado(String numeroAfiliado){
        this.numeroAfiliado = numeroAfiliado;
    }
}
