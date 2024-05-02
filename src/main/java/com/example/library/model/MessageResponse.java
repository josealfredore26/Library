package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The MessageResponse class represents a response object containing a message.
 * It is commonly used in HTTP responses to provide additional information about the request outcome.
 */
@Getter
@Setter
public class MessageResponse {

    /**
     * The message included in the response.
     */
    private String message;

    /**
     * Constructs a new MessageResponse object with the given message.
     * @param message The message to include in the response.
     */
    public MessageResponse(String message) {
        this.message = message;
    }
}
