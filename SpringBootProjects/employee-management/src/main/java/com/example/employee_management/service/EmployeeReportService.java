package com.example.employee_management.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee_management.dto.DepartmentEmployeeCountResponse;
import com.example.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeReportService {

    private final EmployeeRepository employeeRepository;

    public EmployeeReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "employee-count", key = "'total'")
    public long totalEmployees() {
        return employeeRepository.countEmployeesForReport();
    }

    @Transactional(readOnly = true)
    public List<DepartmentEmployeeCountResponse> employeesByDepartment() {
        return employeeRepository.countEmployeesByDepartment();
    }
}
