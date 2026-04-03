package com.SGH.hospital.dto.chat;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRequest {
    private String mensaje;
 
    // Opcional: el frontend puede pedir incluir datos clínicos en el contexto
    private boolean incluirContextoClinico = true;
}