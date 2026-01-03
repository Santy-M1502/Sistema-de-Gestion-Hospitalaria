package com.SGH.hospital.entity; // Paquete de entidades JPA

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity // Marca la clase como entidad persistente
@DiscriminatorValue("MEDICO") // Identifica este tipo en la herencia de Usuario
public class Medico extends Usuario { // Hereda los datos base del usuario

    @Column(nullable = false, unique = true) // Matrícula obligatoria y única
    private String matricula;

    @ManyToMany // Relación muchos a muchos con Especialidad
    @JoinTable(
        name = "medico_especialidad", // Tabla intermedia
        joinColumns = @JoinColumn(name = "medico_id"), // FK hacia Medico
        inverseJoinColumns = @JoinColumn(name = "especialidad_id") // FK hacia Especialidad
    )
    private Set<Especialidad> especialidades = new HashSet<>(); // Evita nulls

    // ---------- Getters y Setters ----------

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Set<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Set<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }
}
