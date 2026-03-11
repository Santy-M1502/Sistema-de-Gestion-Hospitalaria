package com.SGH.hospital.enums;

public enum EstadoReceta {
    ACTIVA("Receta activa"),
    DISPENSADA("Receta dispensada"),
    VENCIDA("Receta vencida");

    private final String descripcion;

    EstadoReceta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
