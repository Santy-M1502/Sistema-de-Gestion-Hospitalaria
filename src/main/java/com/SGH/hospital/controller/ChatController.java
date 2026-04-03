package com.SGH.hospital.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SGH.hospital.chat.AiChatClient;
import com.SGH.hospital.dto.chat.AiHealthResponse;
import com.SGH.hospital.dto.chat.ChatRequest;
import com.SGH.hospital.dto.chat.ChatResponse;
import com.SGH.hospital.service.PacienteChatService;

import java.util.Map;
 
/**
 * ChatController
 * ──────────────
 * Expone el chatbot al frontend del gestor hospitalario.
 *
 * Rutas bajo /api/chat/{pacienteId}/...
 * para que la autenticación/autorización ya sepa qué paciente
 * está involucrado (útil para Spring Security).
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
 
    private final PacienteChatService chatService;
    private final AiChatClient aiChatClient; // Solo para el endpoint de health
 
    // ── POST /api/chat/{pacienteId}/mensaje ───────────────────────────────────
    //
    //  Request body:  { "mensaje": "¿Cuándo es mi próxima consulta?",
    //                   "incluirContextoClinico": true }
    //
    //  Response:      { "respuesta": "...", "modelo": "llama3", ... }
    //
    @PostMapping("/{pacienteId}/mensaje")
    public ResponseEntity<ChatResponse> enviarMensaje(
            @PathVariable Long pacienteId,
            @Valid @RequestBody ChatRequest request) {
 
        ChatResponse response = chatService.enviarMensaje(pacienteId, request);
 
        // Si la IA devolvió error, respondemos 502 (Bad Gateway)
        // para que el frontend sepa que el problema es externo
        if (response.isError()) {
            return ResponseEntity.status(502).body(response);
        }
 
        return ResponseEntity.ok(response);
    }
 
    // ── DELETE /api/chat/{pacienteId}/historial ───────────────────────────────
    //
    //  Limpia el historial de conversación del paciente en el microservicio.
    //  El paciente puede pedirlo desde el frontend ("Empezar nueva conversación").
    //
    @DeleteMapping("/{pacienteId}/historial")
    public ResponseEntity<Map<String, String>> limpiarHistorial(
            @PathVariable Long pacienteId) {
 
        chatService.limpiarHistorial(pacienteId);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Historial de conversación eliminado",
                "pacienteId", pacienteId.toString()
        ));
    }
 
    // ── GET /api/chat/{pacienteId}/estado ─────────────────────────────────────
    //
    //  Devuelve cuántos mensajes tiene la sesión activa.
    //  Útil para que el frontend sepa si hay una conversación en curso.
    //
    @GetMapping("/{pacienteId}/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado(
            @PathVariable Long pacienteId) {
 
        int cantidadMensajes = chatService.obtenerEstadoSesion(pacienteId);
        return ResponseEntity.ok(Map.of(
                "pacienteId", pacienteId,
                "mensajesEnSesion", cantidadMensajes,
                "sesionActiva", cantidadMensajes > 0
        ));
    }
 
    // ── GET /api/chat/health ──────────────────────────────────────────────────
    //
    //  Verifica si el microservicio de IA y Ollama están operativos.
    //  El frontend puede llamarlo antes de mostrar el chat para evitar
    //  mostrar el chatbot si el servicio está caído.
    //
    @GetMapping("/health")
    public ResponseEntity<AiHealthResponse> verificarSalud() {
        AiHealthResponse health = aiChatClient.verificarSalud();
 
        // Si el microservicio está DOWN, devolvemos 503 Service Unavailable
        boolean up = "UP".equalsIgnoreCase(health.getStatus());
        return up
                ? ResponseEntity.ok(health)
                : ResponseEntity.status(503).body(health);
    }
 
    // ── Manejo de errores ─────────────────────────────────────────────────────
 
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }
 
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}