package com.SGH.hospital.dto.chat;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiChatRequest {
    private String sessionId;  // Usaremos el ID/DNI del paciente
    private String mensaje;
}
