package com.meditrack.dto.auth;

public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {}
