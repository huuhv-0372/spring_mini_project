package com.huuhv.mini_project.seeder;

import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.entity.Employee;
import com.huuhv.mini_project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// This class is intended to seed the database with initial employee data with department_id from departments.id
@Component
@RequiredArgsConstructor
@Order(20)
public class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            // Seed data for employees table via saveAll.
            System.out.println("Seeding employee data...");
            employeeRepository.saveAll(
                java.util.List.of(
                    Employee.builder().name("Alice").email("alice@example.com").department(departmentRef(1L)).build(),
                    Employee.builder().name("Bob").email("bob@example.com").department(departmentRef(2L)).build(),
                    Employee.builder().name("Charlie").email("Charlie@example.com").department(departmentRef(3L)).build(),
                    Employee.builder().name("Trump").email("Trump@example.com").department(departmentRef(4L)).build(),
                    Employee.builder().name("Elon Musk").email("Elon_Musk@example.com").department(departmentRef(5L)).build(),
                    Employee.builder().name("HuuHV").email("huuhv@example.com").department(departmentRef(6L)).build()
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
