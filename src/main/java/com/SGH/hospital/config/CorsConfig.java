package com.SGH.hospital.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing)
 * 
 * CORS es un mecanismo de seguridad que controla qué dominios externos
 * pueden hacer peticiones a nuestra API. Sin CORS configurado, un frontend
 * en localhost:3000 no podría comunicarse con el backend en localhost:8080.
 * 
 * Esta clase permite que aplicaciones frontend hospedadas en otros dominios
 * puedan consumir nuestra API de forma segura.
 */
@Configuration  // Indica que esta clase contiene configuración de Spring
public class CorsConfig {

    /**
     * Inyecta el valor de la propiedad 'cors.allowed-origins' desde application.properties
     * Ejemplo: "http://localhost:3000,http://localhost:5173,https://miapp.vercel.app"
     * 
     * @Value lee variables del archivo de configuración y las asigna a esta variable
     */
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Define un Bean que Spring usará para configurar CORS globalmente en la aplicación.
     * Un @Bean es un objeto que Spring gestiona y que puede ser inyectado donde se necesite.
     * 
     * @return CorsConfigurationSource configurado con las reglas de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Crea el objeto de configuración de CORS
        CorsConfiguration configuration = new CorsConfiguration();
        
        /**
         * setAllowedOrigins: Define QUÉ dominios pueden acceder a la API
         * 
         * allowedOrigins.split(",") convierte el string de dominios separados por comas
         * en un array de strings. Por ejemplo:
         * "http://localhost:3000,https://miapp.com" → ["http://localhost:3000", "https://miapp.com"]
         * 
         * Arrays.asList() convierte el array en una lista que Spring puede procesar
         */
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        
        /**
         * setAllowedMethods: Define QUÉ métodos HTTP están permitidos
         * 
         * - GET: Obtener datos (lectura)
         * - POST: Crear nuevos recursos
         * - PUT: Actualizar recursos completos
         * - DELETE: Eliminar recursos
         * - PATCH: Actualizar parcialmente recursos
         * - OPTIONS: Petición previa que hace el navegador para verificar CORS (preflight)
         */
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        /**
         * setAllowedHeaders: Define QUÉ headers HTTP pueden enviarse en las peticiones
         * 
         * "*" significa TODOS los headers están permitidos, incluyendo:
         * - Authorization (para tokens JWT)
         * - Content-Type (para especificar JSON, FormData, etc.)
         * - Custom headers que el frontend pueda necesitar
         */
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        /**
         * setAllowCredentials: Permite enviar cookies y credenciales
         * 
         * true = El frontend puede enviar:
         * - Cookies de sesión
         * - Headers de autenticación (Authorization)
         * - Certificados SSL del cliente
         * 
         * IMPORTANTE: Si allowCredentials es true, NO puedes usar "*" en allowedOrigins,
         * debes especificar dominios exactos por seguridad.
         */
        configuration.setAllowCredentials(true);
        
        /**
         * setMaxAge: Define cuánto tiempo (en segundos) el navegador puede cachear
         * la respuesta del preflight request (OPTIONS)
         * 
         * 3600L = 1 hora (3600 segundos)
         * 
         * Esto mejora el rendimiento porque el navegador no tiene que hacer
         * una petición OPTIONS antes de cada GET/POST durante esa hora.
         */
        configuration.setMaxAge(3600L);

        /**
         * Crea la fuente de configuración basada en URLs
         * Esto aplica la configuración de CORS a rutas específicas de la aplicación
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        /**
         * registerCorsConfiguration: Aplica la configuración CORS a rutas específicas
         * 
         * "/**" es un patrón que significa "todas las rutas"
         * - /api/pacientes → cubierto
         * - /api/medicos → cubierto
         * - /actuator/health → cubierto
         * - cualquier otra ruta → cubierto
         * 
         * Podrías especificar rutas específicas si quisieras:
         * source.registerCorsConfiguration("/api/**", configuration);  // Solo rutas /api/*
         */
        source.registerCorsConfiguration("/**", configuration);
        
        // Retorna la configuración completa para que Spring la use
        return source;
    }
}