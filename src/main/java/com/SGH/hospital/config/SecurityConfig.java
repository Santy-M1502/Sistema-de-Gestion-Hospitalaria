package com.SGH.hospital.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // ==================== ENDPOINTS PÚBLICOS ====================
                // Health checks (para Railway, Docker, monitoreo)
                .requestMatchers("/", "/api/health", "/health").permitAll()
                
                // Autenticación (login, register, refresh)
                .requestMatchers("/api/auth/**").permitAll()
                
                // Registro público de pacientes
                .requestMatchers(HttpMethod.POST, "/api/pacientes").permitAll()
                
                // Endpoints públicos generales
                .requestMatchers("/api/public/**").permitAll()
                
                // Swagger/OpenAPI (documentación de la API)
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                
                // ==================== ENDPOINTS PROTEGIDOS ====================
                // Por rol específico
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/medicos/**").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers("/api/pacientes/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
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

/**
 * ==================== CONFIGURACIÓN DE SEGURIDAD ====================
 * 
 * ENDPOINTS PÚBLICOS (sin autenticación requerida):
 * ✅ GET  /                           → Home de la API
 * ✅ GET  /api/health                 → Health check
 * ✅ GET  /health                     → Health check alternativo
 * ✅ POST /api/auth/register          → Registro de nuevos usuarios
 * ✅ POST /api/auth/login             → Login
 * ✅ POST /api/auth/refresh           → Refresh de tokens
 * ✅ POST /api/pacientes              → Auto-registro de pacientes
 * ✅ ALL  /api/public/**              → Endpoints públicos generales
 * ✅ ALL  /v3/api-docs/**             → Documentación OpenAPI
 * ✅ ALL  /swagger-ui/**              → Swagger UI
 * ✅ ALL  /swagger-ui.html            → Página principal de Swagger
 * 
 * ENDPOINTS PROTEGIDOS POR ROL:
 * 🔒 /api/admin/**                    → Solo ADMIN
 * 🔒 /api/medicos/**                  → MEDICO o ADMIN
 * 🔒 /api/pacientes/**                → PACIENTE, MEDICO o ADMIN
 * 🔒 Cualquier otro endpoint          → Usuario autenticado
 * 
 * NOTAS:
 * - @EnableMethodSecurity permite usar @PreAuthorize en los controladores
 * - SessionCreationPolicy.STATELESS: no usa sesiones (JWT puro)
 * - JwtAuthenticationFilter se ejecuta ANTES del filtro de autenticación de Spring
 * - Swagger UI accesible públicamente para facilitar testing y documentación
 */