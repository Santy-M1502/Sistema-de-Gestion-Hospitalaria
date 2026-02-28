package com.SGH.hospital.exception;

public class AntecedenteNotFoundException extends RuntimeException {
    public AntecedenteNotFoundException(String mensaje) {
        super(mensaje);
    }
}
