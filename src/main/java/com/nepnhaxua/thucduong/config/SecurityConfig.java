package com.nepnhaxua.thucduong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        // Public pages
                        .requestMatchers("/", "/products", "/products/**", "/blog", "/blog/**", "/about", "/login")
                        .permitAll()
                        // API documentation
                        .requestMatchers("/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/actuator/**")
                        .permitAll()
                        // Public API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/blog/**", "/api/health-map/**")
                        .permitAll()
                        // Everything else requires authentication (will be updated when auth is
                        // implemented)
                        .anyRequest().permitAll() // TODO: tighten when JWT auth is added
                );
        return http.build();
    }
}
