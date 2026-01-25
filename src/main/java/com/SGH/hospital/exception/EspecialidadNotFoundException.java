package com.SGH.hospital.exception;

/**
 * Excepcion especialidad no encontrada
 */
public class EspecialidadNotFoundException extends RuntimeException {
    public EspecialidadNotFoundException(String message) {
        super(message);
    }
}
