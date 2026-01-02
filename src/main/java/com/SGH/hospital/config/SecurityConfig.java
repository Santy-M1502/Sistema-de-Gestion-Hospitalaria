package com.SGH.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Seguridad con Spring Security
 * 
 * Spring Security protege automáticamente TODAS las rutas de la aplicación
 * requiriendo autenticación. Esta clase personaliza qué rutas son públicas
 * y qué rutas requieren autenticación.
 * 
 * Sin esta configuración, incluso /api/health requeriría login.
 */
@Configuration  // Indica que esta clase contiene configuración de Spring
@EnableWebSecurity  // Activa la seguridad web de Spring Security en la aplicación
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad.
     * 
     * SecurityFilterChain es el corazón de Spring Security. Es una serie de filtros
     * que interceptan CADA petición HTTP antes de que llegue a los controllers.
     * 
     * Estos filtros verifican:
     * - ¿La ruta es pública o requiere autenticación?
     * - ¿El usuario está autenticado?
     * - ¿El usuario tiene permisos para acceder a esta ruta?
     * - ¿El token JWT es válido? (cuando lo implementemos)
     * 
     * @param http - Objeto que permite configurar la seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /**
             * CSRF (Cross-Site Request Forgery) Protection
             * 
             * CSRF es un ataque donde un sitio malicioso engaña al navegador del usuario
             * para que haga peticiones no autorizadas a nuestro sitio usando las cookies
             * del usuario.
             * 
             * .csrf(csrf -> csrf.disable())
             * 
             * Lo DESHABILITAMOS porque:
             * 1. Usaremos JWT en lugar de cookies de sesión
             * 2. Las APIs REST stateless no necesitan protección CSRF
             * 3. El frontend está en un dominio diferente (CORS ya lo protege)
             * 
             * NOTA: Si usaras cookies de sesión, DEBERÍAS habilitar CSRF.
             */
            .csrf(csrf -> csrf.disable())
            
            /**
             * Configuración de autorización de peticiones HTTP
             * 
             * Define qué rutas son públicas y cuáles requieren autenticación.
             * Se evalúa en ORDEN: la primera regla que coincida se aplica.
             */
            .authorizeHttpRequests(auth -> auth
                /**
                 * requestMatchers: Define patrones de URL que coinciden con rutas específicas
                 * 
                 * .permitAll(): Permite acceso SIN autenticación a estas rutas
                 * 
                 * "/" - Ruta raíz (home)
                 *       Ejemplo: http://localhost:8080/
                 *       Útil para verificar que la API está viva
                 * 
                 * "/api/health" - Endpoint de salud
                 *       Usado por Render, Docker, y herramientas de monitoreo
                 *       para verificar que la aplicación está funcionando
                 * 
                 * "/actuator/**" - Endpoints de Spring Boot Actuator
                 *       /** significa "cualquier subruta"
                 *       Ejemplos: /actuator/health, /actuator/info, /actuator/metrics
                 *       Estos endpoints son útiles para monitoreo y debugging
                 * 
                 * IMPORTANTE: Estas rutas son PÚBLICAS, accesibles sin token ni login
                 */
                .requestMatchers("/", "/api/health", "/actuator/**").permitAll()
                
                /**
                 * .anyRequest().authenticated()
                 * 
                 * TODAS las demás rutas que NO coincidan con los patrones anteriores
                 * REQUIEREN autenticación.
                 * 
                 * Ejemplos de rutas que requerirán autenticación:
                 * - /api/pacientes → Necesita token JWT
                 * - /api/medicos → Necesita token JWT
                 * - /api/citas → Necesita token JWT
                 * - Cualquier otra ruta no listada arriba
                 * 
                 * Cuando implementemos JWT, aquí se verificará:
                 * 1. ¿Existe el header Authorization?
                 * 2. ¿El token JWT es válido?
                 * 3. ¿El token no ha expirado?
                 * 4. ¿El usuario existe en la base de datos?
                 */
                .anyRequest().authenticated()
            );
        
        /**
         * http.build() construye la configuración completa
         * y retorna el SecurityFilterChain configurado
         * 
         * Spring usará esta cadena de filtros para proteger la aplicación
         */
        return http.build();
    }
}

/**
 * FLUJO DE UNA PETICIÓN CON ESTA CONFIGURACIÓN:
 * 
 * 1. Usuario hace petición a /api/health
 *    → Spring Security intercepta
 *    → Verifica requestMatchers
 *    → Coincide con "/api/health"
 *    → .permitAll() permite pasar
 *    → Petición llega al HealthController
 *    → Respuesta exitosa
 * 
 * 2. Usuario hace petición a /api/pacientes SIN token
 *    → Spring Security intercepta
 *    → No coincide con ningún requestMatcher público
 *    → .anyRequest().authenticated() requiere autenticación
 *    → Usuario NO está autenticado
 *    → Spring Security rechaza con 401 Unauthorized
 * 
 * 3. Usuario hace petición a /api/pacientes CON token JWT válido (futuro)
 *    → Spring Security intercepta
 *    → JwtAuthenticationFilter valida el token (lo implementaremos)
 *    → Token es válido
 *    → Usuario se marca como autenticado
 *    → .anyRequest().authenticated() permite pasar
 *    → Petición llega al PacienteController
 *    → Respuesta exitosa
 */