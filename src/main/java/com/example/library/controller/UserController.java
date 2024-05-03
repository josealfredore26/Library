package com.example.library.controller;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.model.MessageResponse;
import com.example.library.model.User;
import com.example.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The UserController class handles HTTP requests related to users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing the list of users and HttpStatus OK if successful
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity containing the user and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the user does not exist
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(@Parameter(description = "ID of the user") @PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return ResponseEntity containing the created user and HttpStatus CREATED if successful,
     *         or HttpStatus BAD_REQUEST if the request is invalid,
     *         or HttpStatus CONFLICT if the user already exists
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User newUser = userService.save(user.getName(), user.getEmail());
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the updated user information
     * @return ResponseEntity containing the updated user and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the user does not exist,
     *         or HttpStatus BAD_REQUEST if the request is invalid,
     *         or HttpStatus CONFLICT if the updated user conflicts with an existing user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User already exists with provided email")
    })
    public ResponseEntity<?> updateUser(@Parameter(description = "ID of the user") @PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.update(id, user.getName(), user.getEmail());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch(UserNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @return ResponseEntity with a success message and HttpStatus OK if successful,
     *         or HttpStatus NOT_FOUND if the user does not exist
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID", description = "Deletes a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(@Parameter(description = "ID of the user") @PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(new MessageResponse("User successfully deleted."), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
