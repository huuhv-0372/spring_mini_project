package com.huuhv.mini_project.seeder;

import com.huuhv.mini_project.entity.Department;
import com.huuhv.mini_project.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// This class is intended to seed the database with initial department data.
@Component
public class DepartmentSeeder implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            // Seed 5 data for departments table by repository via saveAll
            departmentRepository.saveAll(
                java.util.List.of(
                    new Department(null, "Human Resources", null),
                    new Department(null, "Finance", null),
                    new Department(null, "Engineering", null),
                    new Department(null, "Marketing", null),
                    new Department(null, "Sales", null),
                    new Department(null, "Business", null)
                )
            );

            System.out.println("Department has been created, total is: " + departmentRepository.count());
        }
    }

}
