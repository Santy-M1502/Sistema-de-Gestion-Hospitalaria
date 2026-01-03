package com.SGH.hospital.dto.auth;

// Importa anotaciones de validación para los datos del registro
import jakarta.validation.constraints.*;

// Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

// DTO usado para recibir los datos del registro de un usuario
@Data
public class RegisterRequest {

    // Nombre del usuario
    // No puede ser null ni vacío
    // Debe tener entre 2 y 50 caracteres
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50)
    private String nombre;
    
    // Apellido del usuario
    // No puede ser null ni vacío
    // Debe tener entre 2 y 50 caracteres
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50)
    private String apellido;
    
    // Email del usuario
    // No puede ser null ni vacío
    // Debe tener formato de email válido
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    // Contraseña del usuario
    // No puede ser null ni vacía
    // Debe tener al menos 6 caracteres
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    // DNI del usuario
    // No puede ser null ni vacío
    // Debe tener exactamente 8 dígitos numéricos
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;
    
    // Teléfono del usuario
    // No puede ser null ni vacío
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    // Dirección del usuario
    // Campo opcional, puede ser null
    private String direccion;
    
    // Fecha de nacimiento del usuario
    // No puede ser null
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private java.time.LocalDate fechaNacimiento;
}
