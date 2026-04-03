package com.SGH.hospital.chat;

import com.SGH.hospital.dto.chat.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente HTTP para el microservicio de IA.
 * Usa java.net.http.HttpClient (Java 11+) — sin WebFlux ni dependencias extra.
 */
@Slf4j
@Component
public class AiChatClient {

    @Value("${ai.microservicio.url}")
    private String baseUrl;

    // HttpClient y ObjectMapper son thread-safe: se crean una sola vez
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    // ── POST /api/v1/chat ─────────────────────────────────────────────────────

    public AiChatResponse enviarMensaje(String sessionId, String mensaje) {
        try {
            String body = mapper.writeValueAsString(new AiChatRequest(sessionId, mensaje));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/v1/chat"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofSeconds(60)) // Los LLMs pueden tardar
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return mapper.readValue(response.body(), AiChatResponse.class);
            } else {
                log.error("[AI-CLIENT] Error HTTP {} al enviar mensaje", response.statusCode());
                return AiChatResponse.builder()
                        .error("El servicio de IA respondió con error HTTP: " + response.statusCode())
                        .build();
            }

        } catch (Exception ex) {
            log.error("[AI-CLIENT] Sin conexión con microservicio de IA: {}", ex.getMessage());
            return AiChatResponse.builder()
                    .error("No se pudo conectar con el servicio de IA: " + ex.getMessage())
                    .build();
        }
    }

    // ── DELETE /api/v1/chat/{sessionId} ───────────────────────────────────────

    public void limpiarSesion(String sessionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/v1/chat/" + sessionId))
                    .DELETE()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            http.send(request, HttpResponse.BodyHandlers.discarding());
            log.info("[AI-CLIENT] Sesión {} limpiada", sessionId);

        } catch (Exception ex) {
            // No es crítico si falla — el microservicio limpia sesiones viejas solo
            log.warn("[AI-CLIENT] No se pudo limpiar sesión {}: {}", sessionId, ex.getMessage());
        }
    }

    // ── GET /api/v1/chat/{sessionId}/estado ───────────────────────────────────

    public int obtenerCantidadMensajes(String sessionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/v1/chat/" + sessionId + "/estado"))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                AiSesionEstado estado = mapper.readValue(response.body(), AiSesionEstado.class);
                return estado.getCantidadMensajes() != null ? estado.getCantidadMensajes() : 0;
            }

        } catch (Exception ex) {
            log.warn("[AI-CLIENT] No se pudo obtener estado de sesión {}: {}", sessionId, ex.getMessage());
        }
        return 0;
    }

    // ── GET /api/v1/health ────────────────────────────────────────────────────

    public AiHealthResponse verificarSalud() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/v1/health"))
                    .GET()
                    .timeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), AiHealthResponse.class);
            }

        } catch (Exception ex) {
            log.error("[AI-CLIENT] Microservicio de IA no disponible: {}", ex.getMessage());
        }

        return AiHealthResponse.builder()
                .status("DOWN")
                .mensaje("No se pudo conectar con el microservicio de IA")
                .build();
    }
}