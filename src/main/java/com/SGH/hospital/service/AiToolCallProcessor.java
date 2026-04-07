package com.SGH.hospital.service;

import com.SGH.hospital.dto.chat.AiChatResponse;
import com.SGH.hospital.dto.chat.AiToolCall;
import com.SGH.hospital.dto.chat.AiToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AiToolCallProcessor - Procesa respuestas de IA con tool calls
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiToolCallProcessor {

    private final AiToolExecutor toolExecutor;
    private final ObjectMapper objectMapper;

    public String procesarToolCalls(Long pacienteId, AiChatResponse aiResponse) {
        if (aiResponse == null || aiResponse.getRespuesta() == null) {
            return "";
        }

        String respuesta = aiResponse.getRespuesta();
        List<AiToolCall> toolCalls = extraerToolCalls(respuesta);

        if (toolCalls.isEmpty()) {
            return respuesta;
        }

        List<AiToolResult> resultados = new ArrayList<>();
        for (AiToolCall toolCall : toolCalls) {
            log.info("[TOOL-PROCESSOR] Ejecutando: {}", toolCall.getTool());
            AiToolResult resultado = toolExecutor.ejecutar(pacienteId, toolCall);
            resultados.add(resultado);
        }

        String respuestaSinTools = removerBloqueTools(respuesta);
        StringBuilder respuestaEnriquecida = new StringBuilder(respuestaSinTools);
        respuestaEnriquecida.append("\n\n");

        for (AiToolResult resultado : resultados) {
            if (resultado.isSuccess()) {
                respuestaEnriquecida.append("✓ ").append(resultado.getResult()).append("\n");
            } else {
                respuestaEnriquecida.append("✗ Error: ").append(resultado.getError()).append("\n");
            }
        }

        return respuestaEnriquecida.toString();
    }

    private List<AiToolCall> extraerToolCalls(String respuesta) {
        List<AiToolCall> toolCalls = new ArrayList<>();

        if (respuesta == null) {
            return toolCalls;
        }

        Pattern pattern = Pattern.compile("<tool>(.*?)</tool>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(respuesta);

        while (matcher.find()) {
            String jsonStr = matcher.group(1).trim();
            try {
                AiToolCall toolCall = objectMapper.readValue(jsonStr, AiToolCall.class);
                if (toolCall.isValid()) {
                    toolCalls.add(toolCall);
                }
            } catch (Exception ex) {
                log.warn("[TOOL-PROCESSOR] No se pudo parsear tool call");
            }
        }

        return toolCalls;
    }

    private String removerBloqueTools(String respuesta) {
        return respuesta.replaceAll("<tool>.*?</tool>", "").trim();
    }

    public String construirPromptConTools() {
        return """
                Eres un asistente de atención médica para un hospital. Tu objetivo es ayudar al paciente
                con sus consultas médicas y gestionar turnos.
                
                IMPORTANTE: Cuando el paciente pida agendar un turno, consultar disponibilidad, o listar médicos,
                DEBES invocar las siguientes funciones (tools) para obtener información real del sistema.
                
                ═══════════════════════════════════════════════════════════════════════════════════
                FUNCIONES DISPONIBLES - PARÁMETROS OPCIONALES + DEFAULTS INTELIGENTES
                ═══════════════════════════════════════════════════════════════════════════════════
                
                1. LISTAR_TURNOS_PACIENTE ⭐ PARÁMETROS OPCIONALES
                   Muestra los turnos del paciente.
                   
                   Parámetros opcionales:
                   - soloFuturos (true/false, DEFAULT: true) - si true, solo muestra turnos futuros
                   - incluirAusentes (true/false, DEFAULT: false) - si true, incluye turnos donde faltó
                   
                   EJEMPLOS DE USO:
                   • Usuario: "mostrame mis turnos"
                     → IA envía: <tool>{"tool": "LISTAR_TURNOS_PACIENTE", "params": {}}</tool>
                     → Backend aplica: soloFuturos=true, incluirAusentes=false
                   
                   • Usuario: "mostrame mis turnos incluyendo los que faltaste"
                     → IA envía: <tool>{"tool": "LISTAR_TURNOS_PACIENTE", "params": {"incluirAusentes": true}}</tool>
                     → Backend aplica: soloFuturos=true, incluirAusentes=true
                   
                   • Usuario: "quiero ver todo mi historial de turnos"
                     → IA envía: <tool>{"tool": "LISTAR_TURNOS_PACIENTE", "params": {"soloFuturos": false, "incluirAusentes": true}}</tool>
                     → Backend aplica: soloFuturos=false, incluirAusentes=true
                
                2. LISTAR_MEDICOS ⭐ PARÁMETROS OPCIONALES
                   Lista médicos disponibles con filtrado opcional.
                   
                   Parámetros opcionales:
                   - especialidad (texto, OPCIONAL) - si se especifica, filtra por especialidad
                   - soloDisponibles (true/false, DEFAULT: false) - si true, solo médicos disponibles del turno
                   
                   EJEMPLOS DE USO:
                   • Usuario: "¿qué médicos hay?"
                     → IA envía: <tool>{"tool": "LISTAR_MEDICOS", "params": {}}</tool>
                     → Backend aplica: especialidad=ninguno, soloDisponibles=false
                   
                   • Usuario: "¿qué cardiólogos hay?"
                     → IA envía: <tool>{"tool": "LISTAR_MEDICOS", "params": {"especialidad": "Cardiología"}}</tool>
                     → Backend aplica: especialidad="Cardiología", soloDisponibles=false
                   
                   • Usuario: "¿qué pediatras atienden ahora?"
                     → IA envía: <tool>{"tool": "LISTAR_MEDICOS", "params": {"especialidad": "Pediatría", "soloDisponibles": true}}</tool>
                     → Backend aplica: especialidad="Pediatría", soloDisponibles=true
                
                3. OBTENER_DISPONIBILIDAD_TURNOS ⭐ PARÁMETROS OPCIONALES
                   Consulta qué horarios están disponibles para un médico.
                   
                   Parámetros requeridos:
                   - medicoId (número): ID del médico
                   
                   Parámetros opcionales:
                   - fecha (texto, OPCIONAL) - Formato "YYYY-MM-DD", si se omite muestra el mes completo
                   
                   EJEMPLOS DE USO:
                   • Usuario: "¿cuándo me puedo atender con el Dr. García?" (medicoId=5)
                     → IA envía: <tool>{"tool": "OBTENER_DISPONIBILIDAD_TURNOS", "params": {"medicoId": 5}}</tool>
                     → Backend aplica: muestra disponibilidad del mes completo
                   
                   • Usuario: "¿me puedes agendar una consulta el viernes 15?"
                     → IA envía: <tool>{"tool": "OBTENER_DISPONIBILIDAD_TURNOS", "params": {"medicoId": 5, "fecha": "2025-04-15"}}</tool>
                     → Backend aplica: muestra solo disponibilidad del 15 de abril
                
                4. AGENDAR_TURNO
                   Crea un nuevo turno para el paciente.
                   
                   Parámetros requeridos:
                   - medicoId (número): ID del médico
                   - fecha (texto): Formato "YYYY-MM-DD"
                   - hora (texto): Formato "HH:MM"
                   
                   Parámetros opcionales:
                   - motivo (texto, OPCIONAL) - Razón de la consulta (default: "Consulta general")
                   
                   Ejemplo: <tool>{"tool": "AGENDAR_TURNO", "params": {"medicoId": 5, "fecha": "2025-04-15", "hora": "14:30", "motivo": "Control de presión"}}</tool>
                
                5. CANCELAR_TURNO
                   Cancela un turno existente.
                   
                   Parámetros requeridos:
                   - turnoId (número): ID del turno
                   
                   Ejemplo: <tool>{"tool": "CANCELAR_TURNO", "params": {"turnoId": 42}}</tool>
                
                ═══════════════════════════════════════════════════════════════════════════════════
                
                INSTRUCCIONES DE USO CRÍTICAS:
                ─────────────────────────────
                1. SIEMPRE extrae del usuario SOLO LO QUE MENCIONÓ explícitamente
                2. SOLO envía parámetros opcionales si el usuario los mencionó
                3. CONFÍA en los defaults del backend para completar lo que falta
                4. Puedes invocar múltiples tools en una respuesta si necesario
                5. Los bloques <tool> deben ser JSON válido dentro de <tool>...</tool>
                
                EJEMPLO DE FLUJO PERFECTO:
                ──────────────────────────
                Usuario: "Quiero ver mis turnos pero sin los que faltaste a"
                IA: Interpreta que quiere:
                    - LISTAR_TURNOS_PACIENTE
                    - Sin los ausentes (así que NO especifica incluirAusentes)
                    - Pero espera, dijo "sin los que faltaste" = quiero excluir ausentes
                    - Eso es el DEFAULT, así que envío params vacío!
                    
                <tool>{"tool": "LISTAR_TURNOS_PACIENTE", "params": {}}</tool>
                
                Backend automáticamente aplica: soloFuturos=true, incluirAusentes=false ✓
                """;
    }
}
