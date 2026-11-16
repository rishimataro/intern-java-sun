package com.example.employee_management.dto;

public record EmployeeResponse(Long id, String name, String email, Long departmentId, String departmentName) {
}
