package com.example.geoTracker.src.services.interfaces;

import com.example.geoTracker.src.models.Role;
import com.example.geoTracker.src.models.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<User> createUser(User user);
    CompletableFuture<Optional<User>> getUserById(UUID id);
    CompletableFuture<Optional<User>> getUserByEmail(String email);
    CompletableFuture<Optional<User>> updateUserPassword(UUID id, String newPassword);
    CompletableFuture<Optional<User>> updateUserEmail(UUID id, String newEmail);
    CompletableFuture<Optional<User>> updateUserRole(UUID id, Role role);
    CompletableFuture<Optional<User>> changeUserActive(UUID id);
    CompletableFuture<Void> deleteUser(UUID id);
}
