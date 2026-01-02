package com.SGH.hospital.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de Salud de la API
 * 
 * Este controlador proporciona endpoints simples para verificar que:
 * 1. La aplicación está funcionando (no crasheó)
 * 2. El servidor web está respondiendo
 * 3. La API está accesible desde internet
 * 
 * Es usado por:
 * - Render: Para health checks automáticos
 * - Docker: Para verificar el estado del contenedor
 * - Herramientas de monitoreo: Para alertar si la API está caída
 * - Desarrolladores: Para pruebas rápidas
 */
@RestController  // Combina @Controller + @ResponseBody (todas las respuestas son JSON automáticamente)
public class HealthController {

    /**
     * Endpoint raíz de la API
     * 
     * @GetMapping("/") - Mapea peticiones HTTP GET a la ruta raíz "/"
     * 
     * Cuando alguien visita: http://localhost:8080/
     * o: https://tu-app.onrender.com/
     * 
     * Este método se ejecuta y retorna información básica de la API.
     * 
     * @return Map<String, Object> - Un mapa (diccionario) con información
     *         Spring Boot automáticamente lo convierte a JSON
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        /**
         * HashMap: Estructura de datos clave-valor
         * Permite almacenar pares como: "message" → "Texto del mensaje"
         * 
         * <String, Object> significa:
         * - String: Las claves son texto ("message", "status", etc.)
         * - Object: Los valores pueden ser cualquier tipo (String, int, LocalDateTime, etc.)
         */
        Map<String, Object> response = new HashMap<>();
        
        /**
         * .put(clave, valor) - Agrega un par clave-valor al mapa
         * 
         * "message" es la clave, "Sistema de Gestión..." es el valor
         * Cuando se convierte a JSON será: {"message": "Sistema de Gestión..."}
         */
        response.put("message", "Sistema de Gestión Hospitalaria API");
        
        /**
         * Indica que la API está corriendo y operativa
         * Útil para scripts que verifican el estado
         */
        response.put("status", "running");
        
        /**
         * LocalDateTime.now() - Obtiene la fecha y hora actual del servidor
         * 
         * Útil para:
         * - Ver la zona horaria del servidor
         * - Verificar sincronización de tiempo
         * - Debugging de problemas temporales
         * 
         * Ejemplo de salida: "2026-01-01T21:45:30.123"
         */
        response.put("timestamp", LocalDateTime.now());
        
        /**
         * Retorna el mapa completo
         * 
         * Spring Boot automáticamente lo serializa a JSON:
         * {
         *   "message": "Sistema de Gestión Hospitalaria API",
         *   "status": "running",
         *   "timestamp": "2026-01-01T21:45:30.123456"
         * }
         */
        return response;
    }

    /**
     * Endpoint de Health Check
     * 
     * @GetMapping("/api/health") - Mapea GET requests a /api/health
     * 
     * Este endpoint es CRÍTICO para el deployment en Render:
     * - Render hace peticiones automáticas a /api/health cada 30 segundos
     * - Si retorna 200 OK → Render marca la app como "healthy"
     * - Si retorna error o timeout → Render marca la app como "unhealthy" y puede reiniciarla
     * 
     * También es usado por:
     * - Docker healthchecks (HEALTHCHECK en Dockerfile)
     * - Load balancers para distribuir tráfico solo a instancias sanas
     * - Sistemas de monitoreo (Prometheus, Grafana, etc.)
     * 
     * @return Map<String, Object> con el estado de salud
     */
    @GetMapping("/api/health")
    public Map<String, Object> health() {
        // Crea un nuevo mapa para la respuesta de salud
        Map<String, Object> health = new HashMap<>();
        
        /**
         * "UP" es el estándar de Spring Boot Actuator para indicar que todo está bien
         * 
         * Posibles valores estándar:
         * - "UP": Todo funciona correctamente
         * - "DOWN": Hay problemas críticos
         * - "OUT_OF_SERVICE": Servicio deshabilitado temporalmente
         * - "UNKNOWN": Estado desconocido
         * 
         * Render y Docker esperan HTTP 200 + status "UP"
         */
        health.put("status", "UP");
        
        /**
         * Timestamp para saber cuándo se verificó la salud
         * Útil para detectar si la app está "congelada" (siempre retorna el mismo timestamp)
         */
        health.put("timestamp", LocalDateTime.now());
        
        /**
         * Retorna el estado de salud como JSON:
         * {
         *   "status": "UP",
         *   "timestamp": "2026-01-01T21:45:30.123456"
         * }
         * 
         * HTTP Status Code: 200 OK (automático si no hay excepciones)
         */
        return health;
    }
}

/**
 * ¿POR QUÉ ES IMPORTANTE ESTE CONTROLADOR?
 * 
 * 1. DEPLOYMENT:
 *    - Render necesita /api/health para saber que la app está viva
 *    - Sin este endpoint, Render puede marcar la app como "failed" y reiniciarla
 * 
 * 2. DEBUGGING:
 *    - Puedes hacer curl https://tu-app.onrender.com/api/health para verificar rápidamente
 *    - Útil para diagnosticar problemas de red o de deployment
 * 
 * 3. MONITOREO:
 *    - Herramientas de monitoreo pueden hacer ping periódico
 *    - Si no responde, pueden enviar alertas
 * 
 * 4. LOAD BALANCING:
 *    - Load balancers usan health checks para saber a qué instancia enviar tráfico
 *    - Si una instancia está "DOWN", el tráfico se redirige a otras
 * 
 * EJEMPLO DE USO EN PRODUCCIÓN:
 * 
 * $ curl https://hospital-api.onrender.com/api/health
 * {"status":"UP","timestamp":"2026-01-01T21:45:30.123"}
 * 
 * Si ves esto, tu API está funcionando correctamente ✅
 */