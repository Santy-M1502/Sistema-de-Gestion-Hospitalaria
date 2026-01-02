package com.SGH.hospital.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends Usuario {

    @Column(nullable = false, unique = true)
    private String matricula;

    @ManyToMany
    @JoinTable(
        name = "medico_especialidad",
        joinColumns = @JoinColumn(name = "medico_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )

    private Set<Especialidad> especialidades = new HashSet<>();

    public String getMatricula(){
        return this.matricula;
    }

    public void setMatricula(String matricula){
        this.matricula = matricula;
    }

    public Set<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Set<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }
}
