package com.SGH.hospital.exception;

public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resource, String field, String value) {
        super(String.format("%s ya existe con %s: %s", resource, field, value));
    }
}