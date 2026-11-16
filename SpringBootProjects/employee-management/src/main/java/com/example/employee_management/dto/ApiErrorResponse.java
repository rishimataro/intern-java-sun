package com.example.employee_management.dto;

import java.util.Map;

public record ApiErrorResponse(String code, String message, Map<String, String> details) {

    public ApiErrorResponse {
        details = details == null ? Map.of() : Map.copyOf(details);
    }
}
