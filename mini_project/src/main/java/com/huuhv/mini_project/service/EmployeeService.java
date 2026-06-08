package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.repository.DepartmentRepository;
import com.huuhv.mini_project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Helper method to convert Employee entity to EmployeeResponseDTO
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment().getId(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    // Get all employees or search with keyword
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> searchEmployees(String keyword) {
        List<Employee> employees;
        if (keyword == null || keyword.trim().isEmpty()) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.searchByNameOrDepartment(keyword);
        }

        return employees.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Add new employee
    @Transactional
    public EmployeeResponseDTO addEmployee(CreateEmployeeRequestDTO request) {
        // Find department by id
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));

        // Check email exists
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        // Create new employee
        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .department(department)
                .build();

        // Save employee to database
        Employee newEmployee = employeeRepository.save(employee);

        return convertToResponseDTO(newEmployee);
    }
}
