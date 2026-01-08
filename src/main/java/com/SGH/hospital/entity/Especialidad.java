package com.SGH.hospital.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "especialidades")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Boolean activa = true;  // ⭐ CAMPO AGREGADO

    @ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
    private Set<Medico> medicos = new HashSet<>();

    // ==================== Constructores ====================

    public Especialidad() {
    }

    public Especialidad(String nombre) {
        this.nombre = nombre;
        this.activa = true;
    }

    // ==================== Getters y Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActiva() {  // ⭐ GETTER AGREGADO
        return activa;
    }

    public void setActiva(Boolean activa) {  // ⭐ SETTER AGREGADO
        this.activa = activa;
    }

    public Set<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }

    // ==================== Métodos útiles ====================

    /**
     * Verifica si la especialidad está activa
     */
    public boolean estaActiva() {
        return this.activa != null && this.activa;
    }

    /**
     * Activa la especialidad
     */
    public void activar() {
        this.activa = true;
    }

    /**
     * Desactiva la especialidad (soft delete)
     */
    public void desactivar() {
        this.activa = false;
    }

    /**
     * Cuenta cuántos médicos tienen esta especialidad
     */
    public int cantidadMedicos() {
        return this.medicos != null ? this.medicos.size() : 0;
    }
}