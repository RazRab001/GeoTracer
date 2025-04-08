package com.example.geoTracker.utils.dto.user;

import com.example.geoTracker.src.models.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Setter
public class UserGetDto {
    private UUID id;
    private String email;
    private String hashedPassword;
    private String role;
}
