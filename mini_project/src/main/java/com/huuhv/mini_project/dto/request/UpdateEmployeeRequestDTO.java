package com.huuhv.mini_project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequestDTO {

    @NotBlank(message = "Employee name cannot be empty!")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters!")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is incorrect (eg.: example@gmail.com)!")
    private String email;

    @NotNull(message = "Please select department (departmentId)!")
    private Long departmentId;
}
