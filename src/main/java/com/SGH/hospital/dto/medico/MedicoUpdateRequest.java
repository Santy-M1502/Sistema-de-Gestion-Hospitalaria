package com.SGH.hospital.dto.medico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class MedicoUpdateRequest {
    
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    
    @Min(0)
    private Integer aniosExperiencia;
    
    @Size(max = 1000)
    private String biografia;
    
    private Boolean disponible;
    private Set<Long> especialidadIds;

    public MedicoUpdateRequest() {}

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Integer getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(Integer aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public Set<Long> getEspecialidadIds() { return especialidadIds; }
    public void setEspecialidadIds(Set<Long> especialidadIds) { this.especialidadIds = especialidadIds; }
}