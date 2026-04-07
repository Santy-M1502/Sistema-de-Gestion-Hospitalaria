package com.SGH.demo.service;

import com.SGH.hospital.dto.chat.AiToolCall;
import com.SGH.hospital.dto.chat.AiToolResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para AiToolCall")
class AiToolCallTest {

    @Test
    @DisplayName("Debería validar un tool call correcto")
    void testValidarToolCallCorrecto() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("medicoId", 1L);
        AiToolCall toolCall = AiToolCall.builder()
                .tool("LISTAR_MEDICOS")
                .params(params)
                .build();

        // Assert
        assertTrue(toolCall.isValid());
    }

    @Test
    @DisplayName("Debería rechazar tool call sin parámetros")
    void testValidarToolCallSinParams() {
        AiToolCall toolCall = AiToolCall.builder()
                .tool("LISTAR_MEDICOS")
                .params(null)
                .build();

        assertFalse(toolCall.isValid());
    }

    @Test
    @DisplayName("Debería convertir parámetro a Long")
    void testGetParamAsLong() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("medicoId", "5");
        AiToolCall toolCall = AiToolCall.builder()
                .tool("TEST")
                .params(params)
                .build();

        // Act
        Long resultado = toolCall.getParamAsLong("medicoId");

        // Assert
        assertEquals(5L, resultado);
    }

    @Test
    @DisplayName("Debería retornar null si parámetro no existe")
    void testGetParamNoExiste() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        AiToolCall toolCall = AiToolCall.builder()
                .tool("TEST")
                .params(params)
                .build();

        // Act
        Long resultado = toolCall.getParamAsLong("noExiste");

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Debería crear AiToolResult success")
    void testAiToolResultSuccess() {
        // Act
        AiToolResult resultado = AiToolResult.success("TEST", "Operación exitosa");

        // Assert
        assertTrue(resultado.isSuccess());
        assertEquals("TEST", resultado.getTool());
        assertEquals("Operación exitosa", resultado.getResult());
        assertNull(resultado.getError());
    }

    @Test
    @DisplayName("Debería crear AiToolResult error")
    void testAiToolResultError() {
        // Act
        AiToolResult resultado = AiToolResult.error("TEST", "Algo salió mal");

        // Assert
        assertFalse(resultado.isSuccess());
        assertEquals("TEST", resultado.getTool());
        assertEquals("Algo salió mal", resultado.getError());
        assertNull(resultado.getResult());
    }
}
