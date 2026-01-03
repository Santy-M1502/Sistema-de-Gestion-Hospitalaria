package com.SGH.hospital.entity; // Paquete de entidades JPA

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity // Marca la clase como entidad persistente
public class Especialidad {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    private Long id;

    @Column(nullable = false, unique = true) // Nombre obligatorio y único
    private String nombre;

    @ManyToMany(mappedBy = "especialidades") // Lado inverso de la relación con Medico
    private Set<Medico> medicos = new HashSet<>(); // Evita nulls

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }
}