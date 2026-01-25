package com.SGH.hospital.enums;

public enum Rol {

    ADMIN("Administrador"),
    MEDICO("Médico"),
    PACIENTE("Paciente"),
    ENFERMERO("Enfermero");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
