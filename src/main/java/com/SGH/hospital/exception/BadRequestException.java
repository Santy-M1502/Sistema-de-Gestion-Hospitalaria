package com.SGH.hospital.exception;

/**
 * Excepcion request mal enviada
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}