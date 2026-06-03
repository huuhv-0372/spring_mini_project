package com.huuhv.mini_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! Created by Spring Boot. This is a simple RESTful API endpoint.";
    }
}
