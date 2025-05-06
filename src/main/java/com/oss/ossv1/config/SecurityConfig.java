package com.oss.ossv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // public endpoints — no login required
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/products/**"
                        ).permitAll()

                        // future endpoints requiring login
                        .requestMatchers(
                                "/api/cart/**",
                                "/api/orders/**",
                                "/api/user/**"
                        ).authenticated()

                        // any other unspecified routes — allow or secure as needed
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
