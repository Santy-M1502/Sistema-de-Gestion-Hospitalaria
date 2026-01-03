package com.SGH.hospital.enums; // Paquete que contiene los enums del sistema

/**
 * Enum que representa los estados posibles de un usuario
 */
public enum EstadoUsuario {

    // Estados fijos del usuario, cada uno con una descripción
    ACTIVO("Usuario activo en el sistema"),
    INACTIVO("Usuario temporalmente inactivo"),
    SUSPENDIDO("Usuario suspendido por infracción");

    // Descripción asociada a cada estado
    private final String descripcion;

    // Constructor del enum, asigna la descripción a cada valor
    EstadoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve la descripción del estado
    public String getDescripcion() {
        return descripcion;
    }
}
