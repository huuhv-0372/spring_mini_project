package com.huuhv.mini_project.controller.web;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.request.UpdateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.EmployeeRepository;
import com.huuhv.mini_project.service.DepartmentService;
import com.huuhv.mini_project.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final EmployeeRepository employeeRepository;

    // List employees and search
    @GetMapping("/list")
    public String listEmployees(@RequestParam(required = false) String keyword, Model model) {
        List<EmployeeResponseDTO> employees = employeeService.searchEmployees(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);

        return "employees/list";
    }

    // Display add employee form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new CreateEmployeeRequestDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "employees/add";
    }

    // Process employee form (Data Binding)
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
        // Get data employee from DB
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Fill old data to DTO for display form
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

        // Nếu validate bị lỗi (để trống tên, email sai...)
        if (result.hasErrors()) {
            model.addAttribute("employeeId", id);
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/edit"; // Trả lại form báo lỗi
        }

        // Gọi Service cập nhật xuống DB (Đã viết ở Module trước)
        employeeService.updateEmployee(id, request);
        return "redirect:/employees/list"; // Thành công thì quay về trang danh sách
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees/list";
    }
}
