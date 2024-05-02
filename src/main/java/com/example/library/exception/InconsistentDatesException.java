package com.example.library.exception;

/**
 * The InconsistentDatesException class represents an exception that is thrown
 * when dates provided for a loan or a booking are inconsistent, such as when
 * the start date is after the end date.
 */
public class InconsistentDatesException extends RuntimeException {

    /**
     * Constructs a new InconsistentDatesException with the specified detail message.
     *
     * @param message the detail message
     */
    public InconsistentDatesException(String message) {
        super(message);
    }
}
