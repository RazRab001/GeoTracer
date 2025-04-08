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
        name = "activation_tokens",
        indexes = {
                @Index(name = "idx_activation_user_id", columnList = "userId")
        }
)
public class ActivationToken {
    @Id
    private String token;

    @NonNull
    private UUID userId;
}
