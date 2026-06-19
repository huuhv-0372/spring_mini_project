package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.request.UpdateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.DepartmentStatsDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.DepartmentRepository;
import com.huuhv.mini_project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        log.info("New employee added successfully with ID: {}", newEmployee.getId());

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
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());

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

    // Cache result of employee count
    @Cacheable("employeeCount")
    @Transactional(readOnly = true)
    public long getEmployeeCount() {
        log.info("Getting employee count...");

        return employeeRepository.count();
    }

    // Hàm lấy danh sách thống kê
    @Transactional(readOnly = true)
    public List<DepartmentStatsDTO> getEmployeeStatsByDept() {
        return departmentRepository.getEmployeeCountByDepartment();
    }

    // === HÀM TÌM KIẾM CÓ PHÂN TRANG ===
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> searchEmployeesPaginated(String keyword, int pageNo, int pageSize) {

        // PageRequest.of nhận vào index (bắt đầu từ 0)
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Employee> employeePage;

        if (keyword == null || keyword.trim().isEmpty()) {
            employeePage = employeeRepository.findAll(pageable);
        } else {
            employeePage = employeeRepository.searchByNameOrDepartmentPaging(keyword.trim(), pageable);
        }

        // Đối tượng Page của Spring có sẵn hàm map() để chuyển đổi từ Entity sang DTO cực kỳ tiện lợi
        return employeePage.map(this::convertToResponseDTO);
    }

    // Get employee by id
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
