package com.example.library.exception;

/**
 * The UserNotFoundException class represents an exception that is thrown
 * when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

}
