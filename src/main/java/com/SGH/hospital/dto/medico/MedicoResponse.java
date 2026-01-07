package com.SGH.hospital.dto.medico;

import com.SGH.hospital.dto.especialidad.EspecialidadDTO;
import com.SGH.hospital.dto.horarioAtencion.HorarioAtencionDTO;
import com.SGH.hospital.enums.EstadoUsuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class MedicoResponse {
    
    private Long id;
    private String matricula;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private Integer aniosExperiencia;
    private String biografia;
    private Boolean disponible;
    private EstadoUsuario estado;
    private Set<EspecialidadDTO> especialidades;
    private Set<HorarioAtencionDTO> horarios;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MedicoResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Integer getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(Integer aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }

    public Set<EspecialidadDTO> getEspecialidades() { return especialidades; }
    public void setEspecialidades(Set<EspecialidadDTO> especialidades) { this.especialidades = especialidades; }

    public Set<HorarioAtencionDTO> getHorarios() { return horarios; }
    public void setHorarios(Set<HorarioAtencionDTO> horarios) { this.horarios = horarios; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}