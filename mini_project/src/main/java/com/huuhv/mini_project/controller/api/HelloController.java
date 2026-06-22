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

    // API for testing Bean injection into Controller using Lombok
    @GetMapping("/test-lombok")
    public String testLombok() {
        // Use utilityService bean to generate employee code
        String employeeCode = utilityService.generateEmployeeCode(10001L);
        log.info("Employee code is: {}", employeeCode);

        // Use modelMapper bean to map one object to another (example only)
        log.info("ModelMapper Bean: {}", modelMapper);

        // Use passwordEncoder bean to encode a password (example only)
        String password = passwordEncoder.encode("123456");
        // log.info("Password: {}", password);

        return "IoC Container has successfully injected all Beans into the Controller! Check the console for results.";
    }
}
