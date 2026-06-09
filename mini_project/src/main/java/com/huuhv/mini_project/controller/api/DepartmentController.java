package com.huuhv.mini_project.controller.api;

import com.huuhv.mini_project.dto.request.DepartmentRequestDTO;
import com.huuhv.mini_project.dto.response.DepartmentResponseDTO;
import com.huuhv.mini_project.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    // Get all department
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // Get department by Id
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // Add new department
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> addDepartment(
            @Valid
            @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO newDepartment = departmentService.addDepartment(request);
        return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }

    // Update department
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @Valid
            @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    // Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
