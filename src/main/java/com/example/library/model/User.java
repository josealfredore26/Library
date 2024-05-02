package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

/**
 * The User entity represents users who can borrow books from the library.
 * It contains an auto-generated ID, a name, and an email address.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "loan_users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The email address of the user.
     */
    @Email
    private String email;

    /**
     * Constructs a new User object with the given name and email.
     * @param name The name of the user.
     * @param email The email address of the user.
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
