package com.huuhv.mini_project.seeder;

import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// This class is intended to seed the database with initial employee data with department_id from departments.id
@Component
public class EmployeeSeeder implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;

    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            // Seed data for employees table via saveAll.
            System.out.println("Seeding employee data...");
            employeeRepository.saveAll(
                java.util.List.of(
                    new Employee(null, "Alice", "alice@example.com", null, null, departmentRef(1L)),
                    new Employee(null, "Bob", "bob@example.com", null, null, departmentRef(2L)),
                    new Employee(null, "Charlie", "Charlie@example.com", null, null, departmentRef(3L)),
                    new Employee(null, "Trump", "Trump@example.com", null, null, departmentRef(4L)),
                    new Employee(null, "Elon Musk", "Elon_Musk@example.com", null, null, departmentRef(5L)),
                    new Employee(null, "HuuHV", "huuhv@example.com", null, null, departmentRef(6L))
                )
            );
            System.out.println("Employee has been created, total is: " + employeeRepository.count());
        }
    }

    private Department departmentRef(Long id) {
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
