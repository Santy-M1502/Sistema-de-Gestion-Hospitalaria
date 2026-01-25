package com.SGH.hospital.exception;

/**
 * Excepcion no autorizacion
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}