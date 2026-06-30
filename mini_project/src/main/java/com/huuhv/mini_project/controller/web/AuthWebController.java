package com.huuhv.mini_project.controller.web;

import com.huuhv.mini_project.entity.User;
import com.huuhv.mini_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String email,
                                  @RequestParam String role,
                                  Model model) {

        model.addAttribute("oldUsername", username);
        model.addAttribute("oldEmail", email);
        model.addAttribute("oldRole", role);

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("errorMsg", "Tên đăng nhập đã tồn tại!");
            return "auth/register"; // Return to registration page with error
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("errorMsg", "Email này đã được sử dụng!");
            return "auth/register";
        }

        // Check confirm password
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMsg", "Mật khẩu xác nhận không khớp!");
            return "auth/register";
        }


        // Create new user and hash the password
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole(User.Role.ROLE_USER);

        userRepository.save(newUser);

        // Notify success (can also redirect directly to login page)
        model.addAttribute("successMsg", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "auth/register";
    }
}
