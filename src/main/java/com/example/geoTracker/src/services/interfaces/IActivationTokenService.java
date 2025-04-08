package com.example.geoTracker.src.services.interfaces;

import com.example.geoTracker.src.models.ActivationToken;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IActivationTokenService {
    CompletableFuture<ActivationToken> createActivationToken(ActivationToken activationToken);
    CompletableFuture<Optional<ActivationToken>> getActivationTokenByToken(String token);
    CompletableFuture<Optional<ActivationToken>> getActivationTokenByUserId(UUID userId);
    CompletableFuture<Void> deleteActivationToken(String token);
}
