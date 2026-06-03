package com.huuhv.mini_project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data // Tự sinh Getter, Setter, toString...
@Builder // Cho phép khởi tạo object kiểu chain siêu mượt
@NoArgsConstructor // Hàm khởi tạo rỗng
@AllArgsConstructor // Hàm khởi tạo full tham số
public class Employee {
    private Long id;
    private String name;
    private String email;
    private Long department_id;
}
