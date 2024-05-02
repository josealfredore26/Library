package com.example.library.exception;

/**
 * The BookNotFoundException class represents an exception that is thrown
 * when a book cannot be found in the system.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookNotFoundException(String message) {
        super(message);
    }
}
