package com.example.library.exception;

/**
 * The InvalidDataException class represents an exception that is thrown
 * when invalid data is encountered, such as null, blank, or empty values.
 */
public class InvalidDataException extends RuntimeException {

    /**
     * Constructs a new InvalidDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidDataException(String message) {
        super(message);
    }
}
