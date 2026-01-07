package com.SGH.hospital.dto.medico;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

import com.SGH.hospital.dto.horarioAtencion.HorarioAtencionDTO;

public class MedicoRequest {
    
    @NotBlank(message = "La matrícula es obligatoria")
    @Size(max = 20)
    private String matricula;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20)
    private String dni;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8)
    private String password;

    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private Integer aniosExperiencia = 0;
    private String biografia;
    private Boolean disponible = true;

    @NotEmpty(message = "Debe asignar al menos una especialidad")
    private Set<Long> especialidadIds;

    private Set<HorarioAtencionDTO> horarios;

    // Constructor
    public MedicoRequest() {}

    // Getters y Setters
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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    public Set<Long> getEspecialidadIds() { return especialidadIds; }
    public void setEspecialidadIds(Set<Long> especialidadIds) { this.especialidadIds = especialidadIds; }

    public Set<HorarioAtencionDTO> getHorarios() { return horarios; }
    public void setHorarios(Set<HorarioAtencionDTO> horarios) { this.horarios = horarios; }
}