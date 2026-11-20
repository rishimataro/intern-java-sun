package com.example.employee_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.dto.DepartmentEmployeeCountResponse;
import com.example.employee_management.dto.EmployeeCountResponse;
import com.example.employee_management.service.EmployeeReportService;

@RestController
@RequestMapping("/api/employees/report")
public class EmployeeReportController {

    private final EmployeeReportService employeeReportService;

    public EmployeeReportController(EmployeeReportService employeeReportService) {
        this.employeeReportService = employeeReportService;
    }

    @GetMapping("/count")
    public ResponseEntity<EmployeeCountResponse> totalEmployees() {
        return ResponseEntity.ok(new EmployeeCountResponse(employeeReportService.totalEmployees()));
    }

    @GetMapping("/by-department")
    public ResponseEntity<java.util.List<DepartmentEmployeeCountResponse>> employeesByDepartment() {
        return ResponseEntity.ok(employeeReportService.employeesByDepartment());
    }
}
