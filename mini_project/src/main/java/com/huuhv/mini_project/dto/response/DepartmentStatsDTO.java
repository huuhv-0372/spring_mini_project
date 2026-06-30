package com.huuhv.mini_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // REQUIRED: needed for JPQL constructor expression
public class DepartmentStatsDTO {
    private String departmentName;
    private Long employeeCount;
}
