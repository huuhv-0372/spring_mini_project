package com.huuhv.mini_project.repository;

import com.huuhv.mini_project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Relative search (LIKE %...%) ignore case by employee name or department name
    // SELECT * FROM employees e JOIN departments d ON e.department_id = d.id
    // WHERE LOWER(e.name) LIKE LOWER('%keyword%') OR LOWER(d.name) LIKE LOWER('%keyword%');
    @Query("SELECT e FROM Employee e JOIN e.department d " +
            "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByNameOrDepartment(@Param("keyword") String keyword);

    boolean existsByEmail(String email);
}
