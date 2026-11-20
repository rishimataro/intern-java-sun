package com.example.employee_management.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, String username, String role) {
}
