package com.example.employee_management.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_management.component.EmployeeCodeFormatter;
import com.example.employee_management.repository.EmployeeCodeRepository;

@Service
public class UtilityService {

    private final Clock clock;
    private final EmployeeCodeFormatter employeeCodeFormatter;
    private final EmployeeCodeRepository employeeCodeRepository;
    private final AtomicLong employeeSequence = new AtomicLong();

    @Autowired
    public UtilityService(
            Clock clock,
            EmployeeCodeFormatter employeeCodeFormatter,
            EmployeeCodeRepository employeeCodeRepository) {
        this.clock = clock;
        this.employeeCodeFormatter = employeeCodeFormatter;
        this.employeeCodeRepository = employeeCodeRepository;
    }

    public String generateEmployeeCode() {
        String employeeCode = employeeCodeFormatter.format(
                LocalDate.now(clock), employeeSequence.incrementAndGet());
        return employeeCodeRepository.save(employeeCode);
    }
}
