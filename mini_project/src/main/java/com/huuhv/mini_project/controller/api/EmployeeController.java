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

    // API for test inject Bean into Controller
    @GetMapping("/test-ioc")
    public String testIoC() {
        // Sử dụng Bean utilityService để sinh mã nhân viên
        String employeeCode = utilityService.generateEmployeeCode(1L);
        System.out.println("Employee code: " + employeeCode);

        // Sử dụng Bean modelMapper để map 1 đối tượng sang 1 đối tượng khác (ở đây chỉ là ví dụ, không có class cụ thể)
        String mapperHash = modelMapper.toString(); // Chỉ để kiểm tra xem Bean modelMapper có được inject thành công hay không
        System.out.println("ModelMapper Bean: " + mapperHash);

        // Sử dụng Bean passwordEncoder để mã hóa một mật khẩu (ở đây chỉ là ví dụ, không có class cụ thể)
        String password = passwordEncoder.encode("123456");
        System.out.println("Password: " + password);

        return "IoC Container đã inject thành công các Bean vào Controller rồi nhé! Hãy kiểm tra console để xem kết quả.";
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
