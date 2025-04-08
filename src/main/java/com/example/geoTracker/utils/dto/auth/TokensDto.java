package com.example.geoTracker.utils.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}
