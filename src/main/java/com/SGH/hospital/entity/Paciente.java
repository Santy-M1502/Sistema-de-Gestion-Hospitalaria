package com.SGH.hospital.entity; // Paquete de entidades del sistema

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity // Marca la clase como entidad JPA
@DiscriminatorValue("PACIENTE") // Valor que identifica este tipo en la herencia
public class Paciente extends Usuario { // Hereda los campos base de Usuario

    private String obraSocial; // Obra social del paciente

    @Column(nullable = false) // Número obligatorio para identificar al afiliado
    private String numeroAfiliado;

    // ---------- Getters y Setters ----------

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }
}
