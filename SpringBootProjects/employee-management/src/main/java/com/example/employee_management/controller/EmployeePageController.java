package com.example.employee_management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import com.example.employee_management.dto.CreateEmployeeRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.dto.EmployeeForm;
import com.example.employee_management.service.DepartmentService;
import com.example.employee_management.service.EmployeeService;

@Controller
public class EmployeePageController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeePageController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/employees/list")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findEmployees(null, null));
        return "employees/list";
    }

    @GetMapping("/employees/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeForm());
        addDepartments(model);
        return "employees/add";
    }

    @PostMapping("/employees/add")
    public String addEmployee(
            @Valid @ModelAttribute("employeeForm") EmployeeForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            addDepartments(model);
            return "employees/add";
        }

        try {
            employeeService.createEmployee(new CreateEmployeeRequest(
                    form.getName(), form.getEmail(), form.getDepartmentId()));
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode().value() != 404) {
                throw exception;
            }
            bindingResult.rejectValue("departmentId", "department.notFound", exception.getReason());
            addDepartments(model);
            return "employees/add";
        }

        return "redirect:/employees/list";
    }

    @GetMapping("/employees/search")
    public String searchEmployees(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "departmentId", required = false) Long departmentId,
            Model model) {
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("departments", departmentService.findDepartments());
        model.addAttribute("employees", employeeService.findEmployees(name, departmentId));
        return "employees/search";
    }

    private void addDepartments(Model model) {
        List<DepartmentResponse> departments = departmentService.findDepartments();
        model.addAttribute("departments", departments);
    }
}
