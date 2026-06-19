package com.huuhv.mini_project.controller.web;

import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final UserService userService;

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
                                  @RequestParam String email,
                                  Model model) {
        try {
            model.addAttribute("oldUsername", username);
            model.addAttribute("oldEmail", email);
            model.addAttribute("oldRole", role);
            userService.addUser(username, email, password, role);
            model.addAttribute("successMsg", "Đăng ký thành công!");
        } catch (DuplicateResourceException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "auth/register";
        }
        return "auth/register";
    }
}
