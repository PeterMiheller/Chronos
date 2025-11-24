package com.example.chronos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/auth/**").permitAll()
                        
                        // --- SPECIFIC ROUTES FIRST ---
                        
                        // Users
                        .requestMatchers("/api/users/{id}/dashboard-summary").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        .requestMatchers("/api/users/email/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        .requestMatchers("/api/users/{id}").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        
                        // Vacation Requests (Specific)
                        .requestMatchers("/api/vacation-requests/administrator/{id}").hasAnyRole("SUPERADMIN", "ADMINISTRATOR")
                        .requestMatchers("/api/vacation-requests/employee/{id}").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        .requestMatchers("/api/vacation-requests/{requestId}/status").hasAnyRole("SUPERADMIN", "ADMINISTRATOR")
                        
                        // --- GENERIC ROUTES LAST ---
                        
                        .requestMatchers("/api/users/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR")
                        .requestMatchers("/api/companies/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR")
                        .requestMatchers("/api/vacation-requests/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        .requestMatchers("/api/timesheets/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        .requestMatchers("/api/events/**").hasAnyRole("SUPERADMIN", "ADMINISTRATOR", "EMPLOYEE")
                        
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}