package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.request.UpdateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.DepartmentRepository;
import com.huuhv.mini_project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
                employee.getDepartment().getName(),
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
        log.info("Adding new employee: {}", request);
        // Find department by id
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> {
                    log.error("Add employee is failed because department not found with id: {}", request.getDepartmentId());
                    return new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
                });

        // Check email exists
        if (employeeRepository.existsByEmail(request.getEmail())) {
            log.error("Add employee is failed because email already exists: {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Create new employee
        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .department(department)
                .build();

        // Save employee to database
        Employee newEmployee = employeeRepository.save(employee);
        log.info("New employee added successfully: {}", newEmployee);

        return convertToResponseDTO(newEmployee);
    }

    // Update employee
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, UpdateEmployeeRequestDTO request) {
        log.info("Updating employee with id: {} using request: {}", id, request);
        // Find employee by id
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update employee is failed because employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        // Find department by id
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> {
                    log.error("Update employee is failed because department not found with id: {}", request.getDepartmentId());
                    return new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId());
                });

        // Check email exists (exclude current employee)
        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            log.error("Update employee is failed because email already exists: {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        existingEmployee.setName(request.getName());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully: {}", updatedEmployee);

        return convertToResponseDTO(updatedEmployee);
    }

    // Delete employee
    @Transactional
    public void deleteEmployee(Long id) {
        log.warn("Deleting employee with id: {}", id);
        // Find employee by id
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delete employee is failed because employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        employeeRepository.delete(existingEmployee);
        log.info("Employee deleted successfully with id: {}", id);
    }
}
