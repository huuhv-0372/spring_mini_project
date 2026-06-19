package com.huuhv.mini_project.dto.request;

import jakarta.persistence.Column;
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
public class LoginRequestDTO {

    @NotBlank(message = "Username cannot be blank!")
    @Column(nullable = false)
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    @Column(nullable = false)
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters!")
    private String password;
}
