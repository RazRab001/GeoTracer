package com.example.geoTracker.unitTests;

import com.example.geoTracker.src.models.User;
import com.example.geoTracker.src.repositories.UserRepository;
import com.example.geoTracker.src.services.UserServiceImpl;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CreateUser() throws Exception {
        User user = new User();
        Argon2 argon2 = Argon2Factory.create();

        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");

        String password = "test";
        user.setHashedPassword(argon2.hash(2, 65536, 1, password.toCharArray()));
        when(userRepository.save(user)).thenReturn(user);

        CompletableFuture<User> futureUser = userService.createUser(user);
        User result = futureUser.get();

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_Success() throws Exception {
        User user = new User();
        Argon2 argon2 = Argon2Factory.create();

        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setEmail("test@test.com");

        String password = "test";
        user.setHashedPassword(argon2.hash(2, 65536, 1, password.toCharArray()));
        when(userRepository.getUserByIdAndActiveIsTrue(userId)).thenReturn(Optional.of(user));

        CompletableFuture<Optional<User>> futureUser = userService.getUserById(userId);
        Optional<User> result = futureUser.get();

        assertNotNull(result);
        assertEquals(user.getId(), result.get().getId());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).getUserByIdAndActiveIsTrue(userId);
    }

    @Test
    void getUserById_NotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userRepository.getUserByIdAndActiveIsTrue(userId)).thenReturn(Optional.empty());

        CompletableFuture<Optional<User>> futureUser = userService.getUserById(userId);
        Optional<User> result = futureUser.get();

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserByIdAndActiveIsTrue(userId);
    }

    @Test
    void testGetUserById_ExceptionHandling() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.getUserByIdAndActiveIsTrue(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        CompletableFuture<Optional<User>> resultFuture = userService.getUserById(userId);
        Optional<User> result = resultFuture.get();

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserByIdAndActiveIsTrue(userId);
    }

    @Test
    void testUpdateUserPassword() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Argon2 argon2 = Argon2Factory.create();

        String newPassword = "newTest";
        String newHashPassword = argon2.hash(2, 65536, 1, newPassword.toCharArray());

        User updatedUser = new User();
        String password = "test";
        updatedUser.setId(userId);
        updatedUser.setEmail("test@example.com");
        updatedUser.setHashedPassword(argon2.hash(2, 65536, 1, password.toCharArray()));

        doNothing().when(userRepository).updateUserPassword(userId, newHashPassword);
        when(userRepository.getUserByIdAndActiveIsTrue(userId)).thenReturn(Optional.of(updatedUser));

        // Act
        CompletableFuture<Optional<User>> resultFuture = userService.updateUserPassword(userId, newHashPassword);
        Optional<User> result = resultFuture.get();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(updatedUser.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).updateUserPassword(userId, newHashPassword);
        verify(userRepository, times(1)).getUserByIdAndActiveIsTrue(userId);
    }
}
