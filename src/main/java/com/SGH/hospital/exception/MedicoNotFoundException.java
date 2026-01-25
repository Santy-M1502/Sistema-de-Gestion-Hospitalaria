package com.SGH.hospital.exception;

/**
 * Excepcion medico no encontrado
 */
public class MedicoNotFoundException extends RuntimeException {
    public MedicoNotFoundException(String message) {
        super(message);
    }
}