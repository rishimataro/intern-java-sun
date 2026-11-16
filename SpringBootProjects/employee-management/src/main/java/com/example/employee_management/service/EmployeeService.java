package com.example.employee_management.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_management.dto.CreateEmployeeRequest;
import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository, UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.utilityService = utilityService;
    }

    public List<Employee> findEmployees(String name) {
        if (name == null || name.isBlank()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByNameContainingIgnoreCase(name.trim());
    }

    public Employee findEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    public Employee createEmployee(CreateEmployeeRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee name is required");
        }

        Employee employee = new Employee(utilityService.generateEmployeeCode(), request.name().trim());
        return employeeRepository.save(employee);
    }
}
