package com.SGH.hospital.dto.especialidad;

public class EspecialidadDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activa;

    public EspecialidadDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}