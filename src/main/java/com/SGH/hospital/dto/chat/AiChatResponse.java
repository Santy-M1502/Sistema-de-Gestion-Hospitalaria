package com.SGH.hospital.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiChatResponse {
    private String respuesta;
    private String modelo;
    private Integer tokensUsados;
    private Long tiempoMs;
    private String error;      // null si todo fue bien
 
    public boolean tieneError() {
        return error != null && !error.isBlank();
    }
}
