package com.SGH.hospital.exception;

/**
 * Excepcion entidad no existente
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}