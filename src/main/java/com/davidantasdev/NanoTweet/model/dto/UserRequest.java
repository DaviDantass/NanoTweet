package com.davidantasdev.NanoTweet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 1, max = 10, message = "Username must be between 1 and 10 characters")
        @Pattern(regexp = "\\w+", message = "Username must contain only alphanumeric characters and underscores")
        String username
) {
}
