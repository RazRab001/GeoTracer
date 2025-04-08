package com.example.geoTracker.src.services;

import com.example.geoTracker.src.models.Role;
import com.example.geoTracker.src.models.User;
import com.example.geoTracker.src.repositories.UserRepository;
import com.example.geoTracker.src.services.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<User> createUser(User user) {
        return CompletableFuture.supplyAsync(() ->
                userRepository.save(user)
        );
    }

    @Override
    public CompletableFuture<Optional<User>> getUserById(UUID id) {
        return CompletableFuture.supplyAsync(() ->
                userRepository.getUserByIdAndActiveIsTrue(id)
        ).exceptionally(ex -> {
            System.err.println("Error fetching route: " + ex.getMessage());
            return Optional.empty();
        });
    }

    @Override
    public CompletableFuture<Optional<User>> getUserByEmail(String email) {
        return CompletableFuture.supplyAsync(() ->
                userRepository.getUserByEmailAndActiveIsTrue(email)
        ).exceptionally(ex -> {
            System.err.println("Error fetching route: " + ex.getMessage());
            return Optional.empty();
        });
    }

    @Override
    public CompletableFuture<Optional<User>> updateUserPassword(UUID id, String newPassword) {
        CompletableFuture.runAsync(() -> {
            userRepository.updateUserPassword(id, newPassword);
        });
        return getUserById(id);
    }

    @Override
    public CompletableFuture<Optional<User>> updateUserEmail(UUID id, String newEmail) {
        CompletableFuture.runAsync(() -> {
            userRepository.updateUserEmail(id, newEmail);
        });
        return getUserById(id);
    }

    @Override
    public CompletableFuture<Optional<User>> updateUserRole(UUID id, Role role) {
        CompletableFuture.runAsync(() -> {
            userRepository.updateUserRole(id, role);
        });
        return getUserById(id);
    }

    @Override
    public CompletableFuture<Optional<User>> changeUserActive(UUID id) {
        CompletableFuture.runAsync(() -> {
            userRepository.changeUserActive(id);
        });
        return getUserById(id);
    }

    @Override
    public CompletableFuture<Void> deleteUser(UUID id) {
        return CompletableFuture.runAsync(() -> userRepository.deleteUserById(id));
    }
}
