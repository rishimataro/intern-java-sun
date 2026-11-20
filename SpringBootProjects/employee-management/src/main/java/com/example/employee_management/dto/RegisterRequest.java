package com.example.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must contain 3 to 50 characters")
        @Pattern(regexp = "[A-Za-z0-9._-]+", message = "Username may contain letters, numbers, dot, underscore, and hyphen")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain 8 to 72 characters")
        String password) {
}
