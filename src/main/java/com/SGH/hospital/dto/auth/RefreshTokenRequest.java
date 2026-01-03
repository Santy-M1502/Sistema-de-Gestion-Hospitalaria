package com.SGH.hospital.dto.auth;

// Importa la validación para evitar valores null o vacíos
import jakarta.validation.constraints.NotBlank;

// Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

// DTO usado para solicitar un nuevo token usando el refresh token
@Data
public class RefreshTokenRequest {

    // Refresh token enviado por el cliente
    // No puede ser null ni vacío
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
