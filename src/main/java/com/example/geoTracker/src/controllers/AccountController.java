package com.example.geoTracker.src.controllers;

import com.example.geoTracker.src.models.Role;
import com.example.geoTracker.src.models.User;
import com.example.geoTracker.src.services.interfaces.IUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final IUserService userService;

    public AccountController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id:UUID}")
    public DeferredResult<User> getUserById(@PathVariable UUID id) {
        DeferredResult<User> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<User>> futureUser = userService.getUserById(id);

        futureUser.whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(user.get());
            }
        });
        return deferredResult;
    }

    @PutMapping("/change-email/{id:UUID}")
    public DeferredResult<User> updateUserEmail(@PathVariable UUID id, @RequestBody String email) {
        DeferredResult<User> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<User>> futureUser = userService.updateUserEmail(id, email);

        futureUser.whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(user.get());
            }
        });
        return deferredResult;
    }

    @PutMapping("/change-password/{id:UUID}")
    public DeferredResult<User> updateUserPassword(@PathVariable UUID id, @RequestBody String password) {
        DeferredResult<User> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<User>> futureUser = userService.updateUserPassword(id, password);

        futureUser.whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(user.get());
            }
        });
        return deferredResult;
    }

    @PutMapping("/change-role/{id:UUID}")
    public DeferredResult<User> updateUserRole(@PathVariable UUID id, @RequestBody Role role) {
        DeferredResult<User> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<User>> futureUser = userService.updateUserRole(id, role);

        futureUser.whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(user.get());
            }
        });
        return deferredResult;
    }

    @PutMapping("/activation/{id:UUID}")
    public DeferredResult<User> updateUserIsActive(@PathVariable UUID id) {
        DeferredResult<User> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<User>> futureUser = userService.changeUserActive(id);

        futureUser.whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(user.get());
            }
        });
        return deferredResult;
    }

    @DeleteMapping("/{id:UUID}")
    public DeferredResult<Void> deleteUser(@PathVariable UUID id) {
        DeferredResult<Void> deferredResult = new DeferredResult<>();
        CompletableFuture<Void> futureUser = userService.deleteUser(id);

        futureUser.whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            }
            else {
                deferredResult.setResult(result);
            }
        });
        return deferredResult;
    }
}
