package com.example.library.repository;

import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The UserRepository interface provides access to the database for User entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findUserByEmail(String email);

}
