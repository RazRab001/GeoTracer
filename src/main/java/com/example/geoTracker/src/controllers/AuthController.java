package com.example.geoTracker.src.controllers;

import com.example.geoTracker.src.models.ActivationToken;
import com.example.geoTracker.src.models.RefreshToken;
import com.example.geoTracker.src.models.User;
import com.example.geoTracker.src.services.interfaces.IActivationTokenService;
import com.example.geoTracker.src.services.interfaces.IRefreshTokenService;
import com.example.geoTracker.src.services.interfaces.IUserService;
import com.example.geoTracker.utils.TokenGenerator;
import com.example.geoTracker.utils.dto.auth.LoginDto;
import com.example.geoTracker.utils.dto.auth.SignInDto;
import com.example.geoTracker.utils.dto.auth.TokensDto;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUserService userService;
    private final IActivationTokenService activationTokenService;
    private final IRefreshTokenService refreshTokenService;

    private final Argon2 argon2 = Argon2Factory.create();

    public AuthController(IUserService userService, IActivationTokenService activationTokenService,
                          IRefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.activationTokenService = activationTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/reg")
    public DeferredResult<User> registration(@Valid @RequestBody SignInDto userDto) {
        DeferredResult<User> deferredResult = new DeferredResult<>();

        CompletableFuture<User> futureUser = userService.createUser(userDto.toUser(argon2));
        futureUser.thenCompose(user -> {
                    ActivationToken token = new ActivationToken(TokenGenerator.generateUUIDToken(), user.getId());
                    return activationTokenService.createActivationToken(token)
                            .thenApply(saved -> user);
        }).whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            } else {
                deferredResult.setResult(user);
            }
        });
        return deferredResult;
    }

    @PostMapping("/login")
    public DeferredResult<TokensDto> login(@Valid @RequestBody LoginDto userDto) {
        DeferredResult<TokensDto> deferredResult = new DeferredResult<>();

        CompletableFuture<Optional<User>> futureUser = userService.getUserByEmail(userDto.getEmail());
        futureUser.thenCompose(optionalUser -> {
            if (optionalUser.isEmpty()) {
                return CompletableFuture.failedFuture(new RuntimeException("User not found"));
            }

            return CompletableFuture.completedFuture(optionalUser.get());
        }).whenComplete((user, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            } else {
                String refreshToken = TokenGenerator.generateSecureRandomToken();
                refreshTokenService.createRefreshToken(new RefreshToken(refreshToken, user.getId()));

                deferredResult.setResult(new TokensDto(
                        TokenGenerator.generateJWTToken(user.getId(), user.getRole()),
                        refreshToken
                ));
            }
        });
        return deferredResult;
    }

    @GetMapping("/activation/{token}")
    public DeferredResult<TokensDto> activate(@PathVariable String token) {
        DeferredResult<TokensDto> deferredResult = new DeferredResult<>();

        CompletableFuture<Optional<ActivationToken>> futureToken =
                activationTokenService.getActivationTokenByToken(token);

        futureToken.thenCompose(optionalToken -> {
            if (optionalToken.isEmpty()) {
                return CompletableFuture.failedFuture(new RuntimeException("Token not found"));
            }

            ActivationToken activationToken = optionalToken.get();

            activationTokenService.deleteActivationToken(activationToken.getToken());

            return userService.getUserById(activationToken.getUserId()) // Получаем пользователя
                    .thenApply(optionalUser -> {
                        if (optionalUser.isEmpty()) {
                            throw new RuntimeException("User not found");
                        }
                        return optionalUser.get();
                    });
        }).thenApply(user -> {
            String refreshToken = TokenGenerator.generateSecureRandomToken();
            refreshTokenService.createRefreshToken(new RefreshToken(refreshToken, user.getId()));

            return new TokensDto(
                    TokenGenerator.generateJWTToken(user.getId(), user.getRole()), // JWT токен
                    refreshToken
            );
        }).whenComplete((tokensDto, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable.getMessage());
            } else {
                deferredResult.setResult(tokensDto);
            }
        });

        return deferredResult;
    }

    @PutMapping("/refresh/{token}")
    public DeferredResult<TokensDto> refresh(@PathVariable String token) {
        DeferredResult<TokensDto> deferredResult = new DeferredResult<>();

        CompletableFuture<Optional<RefreshToken>> futureToken =
                refreshTokenService.getRefreshTokenByToken(token);

        futureToken.thenCompose(optionalToken -> {
            if (optionalToken.isEmpty()) {
                return CompletableFuture.failedFuture(new RuntimeException("Token not found"));
            }

            RefreshToken refreshToken = optionalToken.get();
            return userService.getUserById(refreshToken.getUserId()) // Получаем пользователя
                    .thenApply(optionalUser -> {
                        if (optionalUser.isEmpty()) {
                            throw new RuntimeException("User not found");
                        }
                        return optionalUser.get();
                    });
        }).thenApply(user -> {
            String newRefreshToken = TokenGenerator.generateSecureRandomToken();
            refreshTokenService.createRefreshToken(new RefreshToken(newRefreshToken, user.getId()));

            return new TokensDto(
                    TokenGenerator.generateJWTToken(user.getId(), user.getRole()), // JWT токен
                    newRefreshToken
            );
        }).whenComplete((tokensDto, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable.getMessage());
            } else {
                deferredResult.setResult(tokensDto);
            }
        });

        return deferredResult;
    }
}
