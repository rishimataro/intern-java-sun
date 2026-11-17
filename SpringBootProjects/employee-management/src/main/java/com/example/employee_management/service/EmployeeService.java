package com.example.employee_management.service;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_management.dto.CreateEmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.model.Department;
import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findEmployees(String name, Long departmentId) {
        String nameFilter = name == null || name.isBlank() ? null : name.trim();
        List<Employee> employees;

        if (nameFilter == null && departmentId == null) {
            employees = employeeRepository.findAllWithDepartment();
        } else if (nameFilter == null) {
            employees = employeeRepository.findByDepartment_Id(departmentId);
        } else if (departmentId == null) {
            employees = employeeRepository.findByNameContainingIgnoreCase(nameFilter);
        } else {
            employees = employeeRepository.findByNameContainingIgnoreCaseAndDepartment_Id(nameFilter, departmentId);
        }

        return employees.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findEmployeeById(Long id) {
        return employeeRepository.findByIdWithDepartment(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        Department department = findDepartment(request.departmentId());
        Employee employee = new Employee(
                request.name().trim(), normalizeEmail(request.email()), department);
        Employee savedEmployee = employeeRepository.save(employee);
        LOGGER.info("Created employee id={} departmentId={}", savedEmployee.id(), department.id());
        return toResponse(savedEmployee);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, CreateEmployeeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        Employee existing = employeeRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        Department department = findDepartment(request.departmentId());
        Employee updated = new Employee(
                existing.id(), request.name().trim(), normalizeEmail(request.email()), department);
        Employee savedEmployee = employeeRepository.save(updated);
        LOGGER.info("Updated employee id={} departmentId={}", savedEmployee.id(), department.id());
        return toResponse(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        employeeRepository.deleteById(id);
        LOGGER.info("Deleted employee id={}", id);
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    private EmployeeResponse toResponse(Employee employee) {
        Department department = employee.department();
        return new EmployeeResponse(
                employee.id(), employee.name(), employee.email(), department.id(), department.name());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
