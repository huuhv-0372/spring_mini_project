package com.huuhv.mini_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // BẮT BUỘC CÓ để dùng trong câu lệnh JPQL
public class DepartmentStatsDTO {
    private String departmentName;
    private Long employeeCount;
}
