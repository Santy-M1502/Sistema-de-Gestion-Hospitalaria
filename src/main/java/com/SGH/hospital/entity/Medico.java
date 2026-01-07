package com.SGH.hospital.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends Usuario {

    // ==================== Campos específicos de Médico ====================

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(nullable = false)
    private Integer aniosExperiencia = 0;

    @Column(length = 1000)
    private String biografia;

    @Column(nullable = false)
    private Boolean disponible = true;

    // ==================== Relaciones ====================

    // Relación ManyToMany con Especialidad
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "medico_especialidad",
        joinColumns = @JoinColumn(name = "medico_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private Set<Especialidad> especialidades = new HashSet<>();

    // Relación OneToMany con HorarioAtencion
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HorarioAtencion> horariosAtencion = new HashSet<>();

    // ==================== Constructores ====================

    public Medico() {
        super();
    }

    // ==================== Métodos auxiliares para gestionar relaciones ====================

    /**
     * Agrega una especialidad al médico y mantiene la bidireccionalidad
     */
    public void addEspecialidad(Especialidad especialidad) {
        this.especialidades.add(especialidad);
        especialidad.getMedicos().add(this);
    }

    /**
     * Remueve una especialidad del médico y mantiene la bidireccionalidad
     */
    public void removeEspecialidad(Especialidad especialidad) {
        this.especialidades.remove(especialidad);
        especialidad.getMedicos().remove(this);
    }

    /**
     * Agrega un horario de atención al médico
     */
    public void addHorario(HorarioAtencion horario) {
        this.horariosAtencion.add(horario);
        horario.setMedico(this);
    }

    /**
     * Remueve un horario de atención del médico
     */
    public void removeHorario(HorarioAtencion horario) {
        this.horariosAtencion.remove(horario);
        horario.setMedico(null);
    }

    /**
     * Limpia todos los horarios del médico
     */
    public void clearHorarios() {
        for (HorarioAtencion horario : this.horariosAtencion) {
            horario.setMedico(null);
        }
        this.horariosAtencion.clear();
    }

    // ==================== Getters y Setters ====================

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(Integer aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Set<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public Set<HorarioAtencion> getHorariosAtencion() {
        return horariosAtencion;
    }

    public void setHorariosAtencion(Set<HorarioAtencion> horariosAtencion) {
        this.horariosAtencion = horariosAtencion;
    }

    // ==================== Métodos útiles ====================

    /**
     * Verifica si el médico tiene una especialidad específica
     */
    public boolean tieneEspecialidad(Long especialidadId) {
        return this.especialidades.stream()
                .anyMatch(e -> e.getId().equals(especialidadId));
    }

    /**
     * Cuenta cuántas especialidades tiene el médico
     */
    public int cantidadEspecialidades() {
        return this.especialidades.size();
    }

    /**
     * Verifica si el médico está activo y disponible para atención
     */
    public boolean estaDisponibleParaAtencion() {
        return this.disponible && this.getEstado() != null 
                && this.getEstado().name().equals("ACTIVO");
    }
}