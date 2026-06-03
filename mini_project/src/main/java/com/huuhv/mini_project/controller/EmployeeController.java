package com.huuhv.mini_project.controller;

import com.huuhv.mini_project.service.UtilityService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    // Khai báo các Bean cần từ khóa final
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection include all params
    public EmployeeController(UtilityService utilityService,
                              ModelMapper modelMapper,
                              PasswordEncoder passwordEncoder) {
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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
}
