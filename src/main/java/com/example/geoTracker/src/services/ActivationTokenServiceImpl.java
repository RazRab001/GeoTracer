package com.example.geoTracker.src.services;

import com.example.geoTracker.src.models.ActivationToken;
import com.example.geoTracker.src.repositories.ActivationTokenRepository;
import com.example.geoTracker.src.services.interfaces.IActivationTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ActivationTokenServiceImpl implements IActivationTokenService {
    private final ActivationTokenRepository activationTokenRepository;

    public ActivationTokenServiceImpl(ActivationTokenRepository activationTokenRepository) {
        this.activationTokenRepository = activationTokenRepository;
    }

    @Override
    public CompletableFuture<ActivationToken> createActivationToken(ActivationToken activationToken) {
        return CompletableFuture.supplyAsync(() -> {
            activationTokenRepository.deleteActivationTokenByUserId(activationToken.getUserId());
            return activationTokenRepository.save(activationToken);
        });
    }

    @Override
    public CompletableFuture<Optional<ActivationToken>> getActivationTokenByToken(String token) {
        return CompletableFuture.completedFuture(activationTokenRepository.findByToken(token));
    }

    @Override
    public CompletableFuture<Optional<ActivationToken>> getActivationTokenByUserId(UUID userId) {
        return CompletableFuture.completedFuture(activationTokenRepository.findByUserId(userId));
    }

    @Override
    public CompletableFuture<Void> deleteActivationToken(String token) {
        return CompletableFuture.completedFuture(activationTokenRepository.deleteActivationTokenByToken(token));
    }
}
