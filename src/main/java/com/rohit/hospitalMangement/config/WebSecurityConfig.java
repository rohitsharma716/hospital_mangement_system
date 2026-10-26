package com.rohit.hospitalMangement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Disable CSRF for REST API testing via Postman / cURL
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Public endpoints
                        .requestMatchers("/public/**").permitAll()

                        // 2. Admin only endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 3. Doctor & Admin endpoints
                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/hospital/**", "/departments/**").hasAnyRole("DOCTOR", "ADMIN")

                        // 4. Patient, Doctor & Admin endpoints
                        .requestMatchers("/patients/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/appointments/**", "/admit/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")

                        // 5. Any other request requires authentication
                        .anyRequest().authenticated()
                )
                // Support both HTTP Basic (for Postman/cURL) and Form Login (for Browser)
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        // ADMIN user
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        // DOCTOR user
        UserDetails doctor = User.withUsername("doctor")
                .password(passwordEncoder.encode("doctor123"))
                .roles("DOCTOR")
                .build();

        // PATIENT user
        UserDetails patient = User.withUsername("patient")
                .password(passwordEncoder.encode("patient123"))
                .roles("PATIENT")
                .build();

        // Legacy / Personal Admin user
        UserDetails rohit = User.withUsername("rohit")
                .password(passwordEncoder.encode("pass"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, doctor, patient, rohit);
    }

}
