package com.SGH.hospital.dto.especialidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EspecialidadRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    private Boolean activa = true;

    public EspecialidadRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}