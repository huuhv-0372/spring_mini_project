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
                                  @RequestParam String role,
                                  Model model) {

        // 1. Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("errorMsg", "Tên đăng nhập đã tồn tại!");
            return "auth/register"; // Trả lại trang đăng ký kèm lỗi
        }

        // 2. Tạo User mới và băm mật khẩu
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);

        userRepository.save(newUser);

        // 3. Báo thành công (có thể redirect thẳng về trang login)
        model.addAttribute("successMsg", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "auth/register";
    }
}
