package com.SGH.hospital.dto.auth;

// Importa anotaciones para validar datos del request
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

// DTO usado para recibir los datos del login
@Data
public class LoginRequest {

    // Campo que recibe el email del usuario
    // @NotBlank evita que llegue vacío o null
    // @Email valida que tenga formato de email
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    // Campo que recibe la contraseña
    // @NotBlank evita que llegue vacía o null
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}