package com.example.library.service;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The UserService class provides business logic for managing User entities.
 * It handles operations such as finding, saving, updating, and deleting users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user if found
     * @throws UserNotFoundException if the user is not found
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Saves a new user to the database.
     *
     * @param name  the name of the user
     * @param email the email of the user
     * @return the saved user
     * @throws InvalidDataException      if the provided data is invalid
     * @throws UserAlreadyExistsException if a user with the same email already exists
     */
    public User save(String name, String email) {
        validateData(name, email);
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with same email");
        }
        return userRepository.save(new User(name, email));
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id    the ID of the user to update
     * @param name  the new name of the user
     * @param email the new email of the user
     * @return the updated user
     * @throws UserNotFoundException     if the user to update is not found
     * @throws InvalidDataException      if the provided data is invalid
     * @throws UserAlreadyExistsException if a user with the same email already exists
     */
    public User update(Long id, String name, String email) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            validateData(name, email);
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isPresent()) {
                User auxUser = userOptional.get();
                if (!Objects.equals(auxUser.getId(), id)) {
                    throw new UserAlreadyExistsException("User already exists with the same email");
                }
            }
            user.setName(name);
            user.setEmail(email);
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user to delete is not found
     */
    public void delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Validates user data.
     *
     * @param name  the name to validate
     * @param email the email to validate
     * @throws InvalidDataException if the provided data is invalid
     */
    public void validateData(String name, String email) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new InvalidDataException("Name cannot be null, blank or empty");
        }
        if (email == null || email.isBlank() || email.isEmpty()) {
            throw new InvalidDataException("Email cannot be null, blank or empty");
        }
    }

}
