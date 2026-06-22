package com.huuhv.mini_project.controller.api;

import com.huuhv.mini_project.dto.request.CreateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.request.UpdateEmployeeRequestDTO;
import com.huuhv.mini_project.dto.response.EmployeeResponseDTO;
import com.huuhv.mini_project.service.EmployeeService;
import com.huuhv.mini_project.service.UtilityService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    // Declaring Beans requires the `final` keyword.
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;

    // Constructor injection include all params
    public EmployeeController(UtilityService utilityService,
                              ModelMapper modelMapper,
                              PasswordEncoder passwordEncoder,
                              EmployeeService employeeService) {
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
    }

    // API for testing Bean injection into Controller
    @GetMapping("/test-ioc")
    public String testIoC() {
        // Use utilityService bean to generate employee code
        String employeeCode = utilityService.generateEmployeeCode(1L);
        System.out.println("Employee code: " + employeeCode);

        // Use modelMapper bean to map one object to another (example only, no specific class)
        String mapperHash = modelMapper.toString(); // Just to verify the modelMapper bean was injected successfully
        System.out.println("ModelMapper Bean: " + mapperHash);

        // Use passwordEncoder bean to encode a password (example only)
        String password = passwordEncoder.encode("123456");
        // System.out.println("Password: " + password);

        return "IoC Container has successfully injected all Beans into the Controller! Check the console for results.";
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword));
    }

    // Add employee to database
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@Valid @RequestBody CreateEmployeeRequestDTO request) {
        EmployeeResponseDTO newEmployee = employeeService.addEmployee(request);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // Update employee to DB
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequestDTO request) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report/count")
    public ResponseEntity<String> getEmployeeReportCount() {
        long total = employeeService.getEmployeeCount();

        return ResponseEntity.ok("Total current employees are: " + total);
    }
}
