package com.meditrack.dto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {}
