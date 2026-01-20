package com.SGH.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.url:http://localhost:8089}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Información general de la API
                .info(new Info()
                        .title("Sistema de Gestión Hospitalaria API")
                        .version("1.0.0")
                        .description("""
                                API REST para la gestión integral de un sistema hospitalario.
                                
                                **Funcionalidades principales:**
                                - Gestión de pacientes
                                - Gestión de médicos y especialidades
                                - Sistema de turnos médicos
                                - Autenticación y autorización con JWT
                                - Control de acceso basado en roles (ADMIN, MEDICO, PACIENTE)
                                
                                **Para usar endpoints protegidos:**
                                1. Registra un usuario en `/api/auth/register`
                                2. Inicia sesión en `/api/auth/login`
                                3. Copia el token de la respuesta
                                4. Haz click en el botón "Authorize" 🔓 arriba
                                5. Pega el token (sin 'Bearer')
                                6. Click en "Authorize"
                                """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@hospital.com")
                                .url("https://github.com/tu-usuario/hospital-api"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                
                // Servidores disponibles
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("Servidor principal")
                ))
                
                // Configuración de seguridad JWT
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del endpoint /api/auth/login (sin el prefijo 'Bearer')")
                        ));
    }
}