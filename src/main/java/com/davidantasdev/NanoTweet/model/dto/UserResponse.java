package com.davidantasdev.NanoTweet.model.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        LocalDateTime createdAt
) {
}
