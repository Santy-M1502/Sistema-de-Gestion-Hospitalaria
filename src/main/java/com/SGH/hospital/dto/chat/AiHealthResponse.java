package com.SGH.hospital.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiHealthResponse {
    private String status;       // "UP" / "DOWN"
    private String ollama;       // Estado de Ollama
    private String mensaje;
}
