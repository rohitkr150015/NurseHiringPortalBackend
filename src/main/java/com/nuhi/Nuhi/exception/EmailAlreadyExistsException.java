package com.nuhi.Nuhi.exception;

// Custom Exceptions
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
