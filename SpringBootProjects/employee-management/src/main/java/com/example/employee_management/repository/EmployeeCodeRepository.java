package com.example.employee_management.repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeCodeRepository {

    private final Set<String> employeeCodes = ConcurrentHashMap.newKeySet();

    public String save(String employeeCode) {
        employeeCodes.add(employeeCode);
        return employeeCode;
    }
}
