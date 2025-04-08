package com.example.geoTracker.src.services.interfaces;

import com.example.geoTracker.src.models.RefreshToken;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRefreshTokenService {
    CompletableFuture<RefreshToken> createRefreshToken(RefreshToken refreshToken);
    CompletableFuture<Optional<RefreshToken>> getRefreshTokenByToken(String token);
    CompletableFuture<Optional<RefreshToken>> getRefreshTokenByUserId(UUID userId);
    CompletableFuture<Void> deleteRefreshToken(String token);
}
