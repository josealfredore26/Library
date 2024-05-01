package com.example.library.exception;

public class NoBookAvailableException extends RuntimeException {

    public NoBookAvailableException(String message) {
        super(message);
    }

}
