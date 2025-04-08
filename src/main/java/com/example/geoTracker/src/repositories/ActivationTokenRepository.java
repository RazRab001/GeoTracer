package com.example.geoTracker.src.repositories;

import com.example.geoTracker.src.models.ActivationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivationTokenRepository extends CrudRepository<ActivationToken, String> {
    Optional<ActivationToken> findByToken(String token);
    Optional<ActivationToken> findByUserId(UUID userId);

    Void deleteActivationTokenByToken(String tokenId);
    void deleteActivationTokenByUserId(UUID userId);
}
