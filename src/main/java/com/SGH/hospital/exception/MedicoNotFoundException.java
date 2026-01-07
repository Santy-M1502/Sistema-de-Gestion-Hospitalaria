package com.SGH.hospital.exception;

public class MedicoNotFoundException extends RuntimeException {
    public MedicoNotFoundException(String message) {
        super(message);
    }
}