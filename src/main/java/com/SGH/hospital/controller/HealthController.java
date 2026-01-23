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
@RestController
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

        Map<String, Object> response = new HashMap<>();
        
        response.put("message", "Sistema de Gestión Hospitalaria API");
        
        response.put("status", "running");
        
        response.put("timestamp", LocalDateTime.now());
        
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

        Map<String, Object> health = new HashMap<>();
        
        health.put("status", "UP");
        
        health.put("timestamp", LocalDateTime.now());
        
        return health;
    }
}