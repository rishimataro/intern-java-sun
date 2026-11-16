package com.example.employee_management.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import com.example.employee_management.dto.CreateDepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findDepartments() {
        return ResponseEntity.ok(departmentService.findDepartments());
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentResponse department = departmentService.createDepartment(request);
        URI location = URI.create("/api/departments/" + department.id());
        return ResponseEntity.created(location).body(department);
    }
}
