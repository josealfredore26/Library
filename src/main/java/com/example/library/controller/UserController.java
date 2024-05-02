package com.example.library.controller;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.model.MessageResponse;
import com.example.library.model.User;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The UserController class handles HTTP requests related to users.
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "User Management")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing the list of users and HttpStatus OK if successful
     */
    @GetMapping
    @ApiOperation(value = "Get all users")
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
    @ApiOperation(value = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
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
    @ApiOperation(value = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 409, message = "User already exists")
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
    @ApiOperation(value = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "User already exists with provided email")
    })
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
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
    @ApiOperation(value = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted successfully"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(new MessageResponse("User successfully deleted."), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
