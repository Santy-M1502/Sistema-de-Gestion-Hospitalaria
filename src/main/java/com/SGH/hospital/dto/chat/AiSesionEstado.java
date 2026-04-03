package com.SGH.hospital.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiSesionEstado {
    private String sessionId;
    private Integer cantidadMensajes;
}
