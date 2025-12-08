package com.davidantasdev.NanoTweet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank(message = "Content cannot be empty")
        @Size(min = 1, max = 42, message = "Content must be between 1 and 42 characters")
        String content
) {
}
