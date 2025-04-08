package com.example.geoTracker.src.repositories;

import com.example.geoTracker.src.models.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(UUID userId);

    @Modifying
    @Query("DELETE RefreshToken rt WHERE rt.token = :token")
    void deleterRefreshTokenByToken(@Param("token") String tokenId);

    void deleteRefreshTokensByUserId(UUID userId);
}
