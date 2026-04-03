package com.SGH.hospital.dto.chat;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatResponse {
    private String respuesta;
    private String modelo;
    private Integer tokensUsados;
    private Long tiempoMs;
    private String pacienteNombre;  // Para personalizar la UI
    private Integer mensajesEnSesion;
    private boolean error;
    private String mensajeError;    // Descripción amigable del error
}