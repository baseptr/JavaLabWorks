package com.esdc.booking.api.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
