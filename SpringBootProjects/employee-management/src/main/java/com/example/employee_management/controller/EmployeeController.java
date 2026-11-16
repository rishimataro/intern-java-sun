package com.example.employee_management.controller;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import com.example.employee_management.dto.CreateEmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.exception.RequestValidationException;
import com.example.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findEmployees(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "departmentId", required = false) Long departmentId) {
        return ResponseEntity.ok(employeeService.findEmployees(name, departmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request,
            BindingResult bindingResult) {
        rejectInvalidRequest(bindingResult);
        EmployeeResponse employee = employeeService.createEmployee(request);
        URI location = URI.create("/api/employees/" + employee.id());
        return ResponseEntity.created(location).body(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody CreateEmployeeRequest request,
            BindingResult bindingResult) {
        rejectInvalidRequest(bindingResult);
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    private void rejectInvalidRequest(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return;
        }

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String message = error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage();
            fieldErrors.putIfAbsent(error.getField(), message);
        }
        throw new RequestValidationException(fieldErrors);
    }
}
