package com.example.library.exception;

public class InconsistentDatesException extends RuntimeException {
    public InconsistentDatesException(String message) {
        super(message);
    }
}
