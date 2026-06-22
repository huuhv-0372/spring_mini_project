package com.huuhv.mini_project.seeder;

import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Order(10)
public class DepartmentSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            // Seed initial data for the departments table
            departmentRepository.saveAll(
                    java.util.List.of(
                            Department.builder().name("Human Resources").build(),
                            Department.builder().name("Finance").build(),
                            Department.builder().name("Engineering").build(),
                            Department.builder().name("Marketing").build(),
                            Department.builder().name("Sales").build(),
                            Department.builder().name("Business").build()
                    )
            );

            System.out.println("Department has been created, total is: " + departmentRepository.count());
        }
    }

}
// This class is intended to seed the database with initial department data.
