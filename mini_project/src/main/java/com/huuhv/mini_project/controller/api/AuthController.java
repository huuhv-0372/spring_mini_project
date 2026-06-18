package com.huuhv.mini_project.controller.api;

import com.huuhv.mini_project.entity.User;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import com.huuhv.mini_project.repository.UserRepository;
import com.huuhv.mini_project.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // API register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default set roll is USER
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    // API login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User loginRequest) {
        // Find user in DB
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + loginRequest.getUsername()));
        // Compare password of user with encode password in DB
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body("Invalid password");
        }
    }
}
