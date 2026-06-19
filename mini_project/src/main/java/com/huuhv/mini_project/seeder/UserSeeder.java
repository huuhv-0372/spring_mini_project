package com.huuhv.mini_project.seeder;

import com.huuhv.mini_project.entity.User;
import com.huuhv.mini_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(30)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Implement your seeding logic here
        if (userRepository.count() == 0) {
            System.out.println("Seeding user data...");
            userRepository.saveAll
                    (List.of(
                            new User(null, "admin", passwordEncoder.encode("123456"), "admin@gmail.com", User.Role.ROLE_ADMIN, null, null),
                            new User(null, "user", passwordEncoder.encode("123456"), "user@gmail.com", User.Role.ROLE_USER, null, null),
                            new User(null, "huuhv", passwordEncoder.encode("123456"), "huuhv@gmail.com", User.Role.ROLE_USER, null, null)
                    ));
            System.out.println("User has been created, total is: " + userRepository.count());
        }
    }
}
