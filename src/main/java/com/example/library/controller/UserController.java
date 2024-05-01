package com.example.library.controller;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.model.MessageResponse;
import com.example.library.model.User;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("User does not found."),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.update(id, user.getName(), user.getEmail());
            if (updatedUser != null) {
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("User does not found."), HttpStatus.NOT_FOUND);
            }
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean success = userService.delete(id);
        if (success) {
            return new ResponseEntity<>(new MessageResponse("User successfully deleted."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("User does not found."), HttpStatus.NOT_FOUND);
        }
    }

}
