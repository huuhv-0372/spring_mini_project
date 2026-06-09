package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.request.DepartmentRequestDTO;
import com.huuhv.mini_project.dto.response.DepartmentResponseDTO;
import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return convertToResponseDTO(department);
    }

    // Add new department
    @Transactional
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department name already exists: " + request.getName());
        }

        Department department = Department.builder()
                .name(request.getName())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        return convertToResponseDTO(savedDepartment);
    }

    // Update department by Id
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (departmentRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("Department name already exists: " + request.getName());
        }

        department.setName(request.getName());
        Department updatedDepartment = departmentRepository.save(department);
        return convertToResponseDTO(updatedDepartment);
    }

    // Delete department by Id
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }
}
