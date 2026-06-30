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

    // Security filter chain for API (uses JWT, no session)
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/actuator/**") // Apply this filter chain to API and actuator endpoints
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/**")
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Allow unauthenticated access to /api/v1/auth/** endpoints
                    .requestMatchers("/api/v1/auth/**").permitAll()

                    // Restrict debug/test endpoints
                    .requestMatchers("/api/v1/test-lombok", "/api/v1/employees/test-ioc").hasRole("ADMIN")
                    // API authorization: USER can view, ADMIN can perform CRUD operations
                    .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/departments/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/v1/employees/**").hasRole("ADMIN") // POST, PUT, DELETE
                    .requestMatchers("/api/v1/departments/**").hasRole("ADMIN") // POST, PUT, DELETE

                    // Require authentication for all other endpoints
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Security filter chain for Web MVC (uses form login, session, Thymeleaf)
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                    // Allow access to the login page and static resources
                    .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()

                    // Only allow ADMIN to access these endpoints
                    .requestMatchers("/employees/add", "/employees/edit/**", "/employees/delete/**").hasRole("ADMIN")
                        // Both USER and ADMIN can access these endpoints
                        .requestMatchers("/employees", "/employees/list").hasAnyRole("ADMIN", "USER")

                    .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/employees/list", true) // Redirect URL after successful login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL to trigger logout
                        .logoutSuccessUrl("/login?logout") // Redirect URL after successful logout
                        .permitAll()
                );

        return http.build();
    }
}
