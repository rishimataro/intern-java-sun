package com.example.employee_management.component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class EmployeeCodeFormatter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    public String format(LocalDate date, long sequence) {
        return String.format(Locale.ROOT, "EMP-%s-%05d", DATE_FORMAT.format(date), sequence);
    }
}
