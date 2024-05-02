package com.example.library.exception;

/**
 * The LoanNotFoundException class represents an exception that is thrown
 * when a loan is not found in the system.
 */
public class LoanNotFoundException extends RuntimeException {

    /**
     * Constructs a new LoanNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public LoanNotFoundException(String message) {
        super(message);
    }
}
