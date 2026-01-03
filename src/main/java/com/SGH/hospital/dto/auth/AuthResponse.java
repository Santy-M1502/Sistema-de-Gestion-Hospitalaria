package com.SGH.hospital.dto.auth;

// Enum que representa el rol del usuario (ADMIN, MEDICO, etc.)
import com.SGH.hospital.enums.Rol;

// Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

// Lombok: permite usar el patrón Builder
import lombok.Builder;

// Lombok: genera constructor vacío
import lombok.NoArgsConstructor;

// Lombok: genera constructor con todos los campos
import lombok.AllArgsConstructor;

// DTO usado para devolver la respuesta de autenticación
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // Token JWT principal para autenticar las requests
    private String token;

    // Token usado para renovar el token principal
    private String refreshToken;

    // Tipo de autenticación
    // Por convención se usa "Bearer" en JWT
    private String type = "Bearer";

    // ID del usuario autenticado
    private Long id;

    // Email del usuario autenticado
    private String email;

    // Nombre del usuario autenticado
    private String nombre;

    // Apellido del usuario autenticado
    private String apellido;

    // Rol del usuario dentro del sistema
    private Rol rol;
}
