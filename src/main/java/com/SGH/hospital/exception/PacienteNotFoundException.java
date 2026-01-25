package com.SGH.hospital.exception;

public class PacienteNotFoundException extends RuntimeException {
    
    /**
     * Excepcion Paciente no encontrado y variables
     */
    public PacienteNotFoundException(String message) {
        super(message);
    }

    public PacienteNotFoundException(Long id) {
        super("Paciente no encontrado con ID: " + id);
    }

    public PacienteNotFoundException(String field, String value) {
        super(String.format("Paciente no encontrado con %s: %s", field, value));
    }
}