package com.example.geoTracker.src.services;

import com.example.geoTracker.src.models.RefreshToken;
import com.example.geoTracker.src.repositories.RefreshTokenRepository;
import com.example.geoTracker.src.services.interfaces.IRefreshTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public CompletableFuture<RefreshToken> createRefreshToken(RefreshToken refreshToken) {
        return CompletableFuture.supplyAsync(() -> {
            refreshTokenRepository.deleteRefreshTokensByUserId(refreshToken.getUserId());
            return refreshTokenRepository.save(refreshToken);
        });
    }

    @Override
    public CompletableFuture<Optional<RefreshToken>> getRefreshTokenByToken(String token) {
        return CompletableFuture.completedFuture(refreshTokenRepository.findByToken(token));
    }

    @Override
    public CompletableFuture<Optional<RefreshToken>> getRefreshTokenByUserId(UUID userId) {
        return CompletableFuture.completedFuture(refreshTokenRepository.findByUserId(userId));
    }

    @Override
    public CompletableFuture<Void> deleteRefreshToken(String token) {
        return CompletableFuture.runAsync(() -> refreshTokenRepository.deleterRefreshTokenByToken(token));
    }
}
