package com.example.employee_management.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Repository;

import com.example.employee_management.model.Employee;

@Repository
public class EmployeeRepository {

    private final ConcurrentSkipListMap<String, Employee> employees = new ConcurrentSkipListMap<>();

    public Employee save(Employee employee) {
        employees.put(employee.id(), employee);
        return employee;
    }

    public List<Employee> findAll() {
        return List.copyOf(employees.values());
    }

    public Optional<Employee> findById(String id) {
        return Optional.ofNullable(employees.get(id));
    }

    public List<Employee> findByNameContainingIgnoreCase(String name) {
        String normalizedName = name.toLowerCase(java.util.Locale.ROOT);
        return employees.values().stream()
                .filter(employee -> employee.name().toLowerCase(java.util.Locale.ROOT).contains(normalizedName))
                .toList();
    }
}
