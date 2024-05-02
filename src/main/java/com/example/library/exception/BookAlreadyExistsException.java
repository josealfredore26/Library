package com.example.library.exception;

/**
 * The BookAlreadyExistsException class represents an exception that is thrown
 * when attempting to add a book that already exists in the system.
 */
public class BookAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new BookAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
