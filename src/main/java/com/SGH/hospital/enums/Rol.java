package com.SGH.hospital.enums; // Paquete donde se agrupan los enums del sistema

/**
 * Enum que define los roles posibles de un usuario
 */
public enum Rol {

    // Valores fijos del enum, cada uno con una descripción legible
    ADMIN("Administrador"),
    MEDICO("Médico"),
    PACIENTE("Paciente"),
    ENFERMERO("Enfermero");

    // Texto descriptivo asociado a cada rol
    private final String descripcion;

    // Constructor del enum, se ejecuta por cada valor declarado arriba
    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve la descripción del rol
    public String getDescripcion() {
        return descripcion;
    }
}
