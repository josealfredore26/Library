package com.example.library.service;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(String name, String email) {
        validateData(name, email);
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with same email");
        }
        return userRepository.save(new User(name, email));
    }

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
        }
        return null;
    }

    public boolean delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public void validateData(String name, String email) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new InvalidDataException("Name cannot be null, blank or empty");
        }
        if (email == null || email.isBlank() || email.isEmpty()) {
            throw new InvalidDataException("Email cannot be null, blank or empty");
        }
    }

}
