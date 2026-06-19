package com.huuhv.mini_project.controller.api;

import com.huuhv.mini_project.dto.request.LoginRequestDTO;
import com.huuhv.mini_project.dto.request.RegisterRequestDTO;
import com.huuhv.mini_project.dto.response.LoginResponseDTO;
import com.huuhv.mini_project.dto.response.UserResponseDTO;
import com.huuhv.mini_project.security.JwtUtil;
import com.huuhv.mini_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // API register
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserResponseDTO newUser = userService.addUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getRole());

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // API login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO newToken = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(newToken);
    }
}
