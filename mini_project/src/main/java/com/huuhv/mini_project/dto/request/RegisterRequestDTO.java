package com.huuhv.mini_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters!")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank!")
    @Size(min = 6, max = 255, message = "Confirm password must be between 6 and 255 characters!")
    private String confirmPassword;

    @NotBlank(message = "Email cannot be blank!")
    @Size(min = 4, max = 100, message = "Email must be between 4 and 100 characters!")
    private String email;

    @NotBlank(message = "Role cannot be blank!")
    private String role;

}
