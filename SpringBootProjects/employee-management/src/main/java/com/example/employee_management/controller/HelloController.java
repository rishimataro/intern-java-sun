package com.example.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.service.UtilityService;

@RestController
public class HelloController {

    private final UtilityService utilityService;

    public HelloController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Xin chao";
    }

    @GetMapping("/employees/code")
    public String generateEmployeeCode() {
        return utilityService.generateEmployeeCode();
    }
}
