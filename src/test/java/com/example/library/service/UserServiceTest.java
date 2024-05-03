package com.example.library.service;

import com.example.library.exception.InvalidDataException;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The UserServiceTest class contains unit tests for the UserService class.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(new User("John", "john@example.com")));

        // Act
        List<User> users = userService.findAll();

        // Assert
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("john@example.com", users.get(0).getEmail());
    }

    @Test
    public void testFindById_UserExists() {
        // Arrange
        Long id = 1L;
        User user = new User("John", "john@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testFindById_UserNotFound() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    public void testSave_UserDoesNotExist() {
        // Arrange
        String name = "John";
        String email = "john@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.save(name, email);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testSave_UserAlreadyExists() {
        // Arrange
        String name = "John";
        String email = "john@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(name, email));
    }

    @Test
    public void testSave_InvalidName() {
        // Arrange & Act & Assert
        assertThrows(InvalidDataException.class, () -> userService.save(null, "john@example.com"));
        assertThrows(InvalidDataException.class, () -> userService.save("", "john@example.com"));
        assertThrows(InvalidDataException.class, () -> userService.save("  ", "john@example.com"));
    }

    @Test
    public void testSave_InvalidEmail() {
        // Arrange & Act & Assert
        assertThrows(InvalidDataException.class, () -> userService.save("John", null));
        assertThrows(InvalidDataException.class, () -> userService.save("John", ""));
        assertThrows(InvalidDataException.class, () -> userService.save("John", "  "));
    }

    @Test
    public void testUpdate_UserExists() {
        // Arrange
        Long id = 1L;
        String name = "John";
        String email = "john@example.com";
        User existingUser = new User("Existing", "existing@example.com");
        existingUser.setId(1L);
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.update(id, name, email);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testUpdate_UserNotFound() {
        // Arrange
        Long id = 1L;
        String name = "John";
        String email = "john@example.com";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.update(id, name, email));
    }

    @Test
    public void testUpdate_UserAlreadyExists() {
        // Arrange
        Long id = 1L;
        String name = "John";
        String email = "john@example.com";
        User existingUser = new User("Existing", "existing@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.update(id, name, email));
    }

    @Test
    public void testUpdate_InvalidName() {
        // Arrange & Act & Assert
        User existingUser = new User("Existing", "existing@example.com");
        existingUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, null, "john@example.com"));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, "", "john@example.com"));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, "  ", "john@example.com"));
    }

    @Test
    public void testUpdate_InvalidEmail() {
        // Arrange & Act & Assert
        User existingUser = new User("Existing", "existing@example.com");
        existingUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, "John", null));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, "John", ""));
        assertThrows(InvalidDataException.class, () -> userService.update(1L, "John", "  "));
    }

    @Test
    public void testDelete_UserExists() {
        // Arrange
        Long id = 1L;
        User existingUser = new User("John", "john@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        // Act
        userService.delete(id);

        // Assert
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    public void testDelete_UserNotFound() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.delete(id));
    }
}

