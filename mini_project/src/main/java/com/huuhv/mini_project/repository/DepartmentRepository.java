package com.huuhv.mini_project.repository;

import com.huuhv.mini_project.dto.response.DepartmentStatsDTO;
import com.huuhv.mini_project.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    // Thống kê số lượng nhân viên theo phòng ban
    @Query("SELECT new com.huuhv.mini_project.dto.response.DepartmentStatsDTO(d.name, COUNT(e.id)) " +
            "FROM Department d LEFT JOIN d.employees e " +
            "GROUP BY d.id, d.name")
    List<DepartmentStatsDTO> getEmployeeCountByDepartment();
}
