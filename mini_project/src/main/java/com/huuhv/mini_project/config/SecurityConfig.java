package com.huuhv.mini_project.config;

import com.huuhv.mini_project.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    // Thread for API (use JWT, no session)
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Cho phép truy cập vào các endpoint /api/v1/auth/** mà không cần xác thực
                    .requestMatchers("/api/v1/auth/**").permitAll()

                    // Phân quyền API: USER xem, ADMIN thao tác CRUD
                    .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/departments/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/v1/employees/**").hasRole("ADMIN") // POST, PUT, DELETE
                    .requestMatchers("/api/v1/departments/**").hasRole("ADMIN") // POST, PUT, DELETE

                    .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

                    // Yêu cầu xác thực cho tất cả các endpoint khác
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Thread for Web MVC (use Form login, Session, Thymeleaf)
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    // Cho phép truy cập vào trang login và tài nguyên tĩnh
                    .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()

                    // Chỉ cho phép ADMIN truy cập các endpoint này
                    .requestMatchers("/employees/add", "/employees/edit/**", "/employees/delete/**").hasRole("ADMIN")
                        // USER vs ADMIN truy cập được các endpoint này
                        .requestMatchers("/employees", "/employees/list").hasAnyRole("ADMIN", "USER")

                    .anyRequest().authenticated() // Yêu cầu xác thực cho tất cả các endpoint khác
                )
                .formLogin(form -> form
                        .loginPage("/login") // Trang login tùy chỉnh
                        .defaultSuccessUrl("/employees/list", true) // Trang chuyển hướng sau khi đăng nhập thành công
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để đăng xuất
                        .logoutSuccessUrl("/login?logout") // Trang chuyển hướng sau khi đăng xuất thành công
                        .permitAll()
                );

        return http.build();
    }
}
