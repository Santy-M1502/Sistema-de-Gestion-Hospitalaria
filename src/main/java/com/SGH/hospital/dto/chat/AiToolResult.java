package com.SGH.hospital.dto.chat;

import lombok.*;

/**
 * AiToolResult
 * ────────────
 * Resultado de ejecutar una tool/función.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiToolResult {
    private String tool;
    private boolean success;
    private String result;
    private String error;

    public static AiToolResult success(String tool, String result) {
        return AiToolResult.builder()
                .tool(tool)
                .success(true)
                .result(result)
                .build();
    }

    public static AiToolResult error(String tool, String error) {
        return AiToolResult.builder()
                .tool(tool)
                .success(false)
                .error(error)
                .build();
    }
}
