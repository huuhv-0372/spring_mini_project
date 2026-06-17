package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.request.DepartmentRequestDTO;
import com.huuhv.mini_project.dto.response.DepartmentResponseDTO;
import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // Helper: Convert Department entity to DepartmentResponseDTO
    private DepartmentResponseDTO convertToResponseDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    // Get all department
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get a department by Id
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Get department is failed because department not found with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });
        return convertToResponseDTO(department);
    }

    // Add new department
    @Transactional
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO request) {
        log.info("Adding new department: {}", request);
        if (departmentRepository.existsByName(request.getName())) {
            log.error("Add department is failed because department name already exists: {}", request.getName());
            throw new DuplicateResourceException("Department name already exists: " + request.getName());
        }

        Department department = Department.builder()
                .name(request.getName())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department added successfully: {}", savedDepartment);
        return convertToResponseDTO(savedDepartment);
    }

    // Update department by Id
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        log.info("Updating department with id {}: {}", id, request);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update department is failed because department not found with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });

        if (departmentRepository.existsByNameAndIdNot(request.getName(), id)) {
            log.error("Update department is failed because department name already exists: {}", request.getName());
            throw new DuplicateResourceException("Department name already exists: " + request.getName());
        }

        department.setName(request.getName());
        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated successfully: {}", updatedDepartment);
        return convertToResponseDTO(updatedDepartment);
    }

    // Delete department by Id
    @Transactional
    public void deleteDepartment(Long id) {
        log.warn("Deleting department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delete department is failed because department not found with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });
        departmentRepository.delete(department);
        log.info("Department deleted successfully: {}", department);
    }
}
