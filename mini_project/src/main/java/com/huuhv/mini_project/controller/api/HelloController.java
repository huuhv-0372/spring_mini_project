package com.huuhv.mini_project.controller.api;

import com.huuhv.mini_project.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HelloController {
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! Created by Spring Boot. This is a simple RESTful API endpoint.";
    }

    // API for test inject Bean into Controller using Lombok
    @GetMapping("/test-lombok")
    public String testLombok() {
        // Sử dụng Bean utilityService để sinh mã nhân viên
        String employeeCode = utilityService.generateEmployeeCode(10001L);
        log.info("Employee code is: {}", employeeCode);

        // Sử dụng Bean modelMapper để map 1 đối tượng sang 1 đối tượng khác (ở đây chỉ là ví dụ, không có class cụ thể)
        log.info("ModelMapper Bean: {}", modelMapper);

        // Sử dụng Bean passwordEncoder để mã hóa một mật khẩu (ở đây chỉ là ví dụ, không có class cụ thể)
        String password = passwordEncoder.encode("123456");
        log.info("Password: {}", password);

        return "IoC Container đã inject thành công các Bean vào Controller rồi nhé! Hãy kiểm tra console để xem kết quả.";
    }
}
