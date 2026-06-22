package com.huuhv.mini_project.controller.web;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.request.UpdateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.DepartmentStatsDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.service.DepartmentService;
import com.huuhv.mini_project.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeWebController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    // List employees and search
    @GetMapping("/list")
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page, // Default to page 1
            @RequestParam(defaultValue = "9") int size, // Default to 9 employees per page
            Model model) {

        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);

        // Call Service to retrieve paginated data
        Page<EmployeeResponseDTO> pageData = employeeService.searchEmployeesPaginated(keyword, safePage, safeSize);

        // Push data to Model for Thymeleaf rendering
        model.addAttribute("employees", pageData.getContent()); // Employee list to display
        model.addAttribute("currentPage", safePage);                // Current page
        model.addAttribute("totalPages", pageData.getTotalPages()); // Total pages
        model.addAttribute("totalItems", pageData.getTotalElements()); // Total employees
        model.addAttribute("keyword", keyword); // Retain keyword in the search bar

        return "employees/list";
    }

    // Display add employee form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new CreateEmployeeRequestDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "employees/add";
    }

    // Process add employee form (Data Binding)
    @PostMapping("/add")
    public String addEmployee(
            @Valid @ModelAttribute("employee") CreateEmployeeRequestDTO request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/add";
        }

        employeeService.addEmployee(request);

        return "redirect:/employees/list";
    }

    // Display edit employee form
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        // Get employee data from DB
        Employee employee = employeeService.getEmployeeById(id);
        // Populate DTO with existing data for the edit form
        UpdateEmployeeRequestDTO employeeRequestDTO = new UpdateEmployeeRequestDTO();
        employeeRequestDTO.setName(employee.getName());
        employeeRequestDTO.setEmail(employee.getEmail());
        if (employee.getDepartment() != null) {
            employeeRequestDTO.setDepartmentId(employee.getDepartment().getId());
        }

        model.addAttribute("employee", employeeRequestDTO);
        model.addAttribute("employeeId", id);
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "employees/edit";
    }

    // Process edit form
    @PostMapping("/edit/{id}")
    public String submitEditForm(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") UpdateEmployeeRequestDTO request,
            BindingResult result,
            Model model) {

        // If validation fails (blank name, invalid email, etc.)
        if (result.hasErrors()) {
            model.addAttribute("employeeId", id);
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/edit"; // Return form with validation errors
        }

        // Call Service to update in DB
        employeeService.updateEmployee(id, request);
        return "redirect:/employees/list"; // On success, redirect to the employee list
    }

    // Delete employee
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees/list";
    }

    // STATISTICS PAGE (accessible to both Admin and User)
    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        // Get total count
        long totalEmployees = employeeService.getTotalEmployeesCount();

        // Get statistics by department
        List<DepartmentStatsDTO> stats = employeeService.getEmployeeStatsByDept();

        // Push data to View
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("stats", stats);

        return "employees/statistics";
    }
}
