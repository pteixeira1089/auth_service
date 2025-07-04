package com.eemarisademello.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disables CSRF for API JWT authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/Oauth/google/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
