package com.huuhv.mini_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF để gọi được POST/PUT/DELETE trên Postman
            .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                // .requestMatchers("/hello").permitAll() // Cho phép truy cập vào endpoint /hello mà không cần xác thực
                // .anyRequest().authenticated() // Yêu cầu xác thực cho tất cả các endpoint khác
            );

        return http.build();
    }
}
