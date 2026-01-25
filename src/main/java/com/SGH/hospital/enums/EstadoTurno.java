package com.SGH.hospital.enums;

public enum EstadoTurno {

    PENDIENTE("Turno pendiente"),
    CONFIRMADO("Turno confirmado"),
    CANCELADO("Turno cancelado"),
    COMPLETADO("Turno completado"),
    AUSENTE("Paciente ausente");

    private final String descripcion;

    EstadoTurno(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
