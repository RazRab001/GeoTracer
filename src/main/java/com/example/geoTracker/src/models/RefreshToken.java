package com.example.geoTracker.src.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_user_id", columnList = "userId")
        }
)
public class RefreshToken {
    @Id
    private String token;

    @NonNull
    private UUID userId;
}
