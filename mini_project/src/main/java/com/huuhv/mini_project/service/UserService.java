package com.huuhv.mini_project.service;

import com.huuhv.mini_project.dto.response.LoginResponseDTO;
import com.huuhv.mini_project.dto.response.UserResponseDTO;
import com.huuhv.mini_project.entity.User;
import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.repository.UserRepository;
import com.huuhv.mini_project.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Helper method convert User entity to UserResponseDTO
    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // Add new user
    @Transactional
    public UserResponseDTO addUser(String username, String email, String password, String role) {
        log.info("Adding new user with username: {}", username);
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            log.error("Username {} is already taken!", username);
            throw new DuplicateResourceException("Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            log.error("Email {} is already taken!", email);
            throw new DuplicateResourceException("Email is already taken!");
        }

        // Create new user and save to database
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Password should be encoded before saving
        user.setRole(User.Role.valueOf(role.toUpperCase())); // Convert role string to enum

        userRepository.save(user);
        log.info("User added successfully with username: {}", username);

        return convertToResponseDTO(user);
    }

    // Login user
    @Transactional(readOnly = true)
    public LoginResponseDTO loginUser(String username, String password) {
        log.info("Logging in user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new BadCredentialsException("Invalid username or password");
                });

        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("User logged in successfully with username: {}", username);
            String token = jwtUtil.generateToken(user.getUsername(), String.valueOf(user.getRole()));

            return new LoginResponseDTO(token);
        } else {
            log.error("Invalid password for username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
