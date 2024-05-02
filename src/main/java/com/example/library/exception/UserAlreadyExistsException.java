package com.example.library.exception;

/**
 * The UserAlreadyExistsException class represents an exception that is thrown
 * when a user with the same email already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
