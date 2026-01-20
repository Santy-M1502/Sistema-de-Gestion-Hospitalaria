package com.SGH.hospital.enums;

public enum EstadoTurno {

    // Estados fijos del usuario, cada uno con una descripción
    PENDIENTE("Turno pendiente"),
    CONFIRMADO("Turno confirmado"),
    CANCELADO("Turno cancelado"),
    COMPLETADO("Turno completado"),
    AUSENTE("Paciente ausente");

    // Descripción asociada a cada estado
    private final String descripcion;

    // Constructor del enum, asigna la descripción a cada valor
    EstadoTurno(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve la descripción del estado
    public String getDescripcion() {
        return descripcion;
    }
}
