package com.SGH.hospital.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

/**
 * AiToolCall
 * ──────────
 * Representa una acción (tool call) que la IA invoca.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiToolCall {
    private String tool;
    private Map<String, Object> params;

    public boolean isValid() {
        return tool != null && !tool.trim().isEmpty() && params != null;
    }

    public String getParamAsString(String key) {
        Object value = params.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getParamAsLong(String key) {
        Object value = params.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }

    public Integer getParamAsInteger(String key) {
        Object value = params.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    public Boolean getParamAsBoolean(String key) {
        Object value = params.get(key);
        if (value == null) return null;
        if (value instanceof Boolean) return (Boolean) value;
        String str = value.toString().toLowerCase();
        return str.equals("true") || str.equals("1") || str.equals("yes");
    }
}
