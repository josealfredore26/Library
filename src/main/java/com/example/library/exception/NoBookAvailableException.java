package com.example.library.exception;

/**
 * The NoBookAvailableException class represents an exception that is thrown
 * when there are no books available for a particular operation.
 */
public class NoBookAvailableException extends RuntimeException {

    /**
     * Constructs a new NoBookAvailableException with the specified detail message.
     *
     * @param message the detail message
     */
    public NoBookAvailableException(String message) {
        super(message);
    }

}
