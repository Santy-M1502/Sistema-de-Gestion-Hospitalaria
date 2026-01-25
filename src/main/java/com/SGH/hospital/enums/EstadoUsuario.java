package com.SGH.hospital.enums;
public enum EstadoUsuario {

    ACTIVO("Usuario activo en el sistema"),
    INACTIVO("Usuario temporalmente inactivo"),
    SUSPENDIDO("Usuario suspendido por infracción");

    private final String descripcion;

    EstadoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
