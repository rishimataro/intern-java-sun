package com.example.employee_management.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_management.dto.CreateDepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.model.Department;
import com.example.employee_management.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findDepartments() {
        return departmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        Department department = departmentRepository.save(new Department(request.name().trim()));
        return toResponse(department);
    }

    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(department.id(), department.name());
    }
}
