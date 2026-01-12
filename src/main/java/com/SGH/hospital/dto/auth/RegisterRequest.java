package com.SGH.hospital.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50)
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    private String direccion;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private java.time.LocalDate fechaNacimiento;
    
    @NotBlank(message = "La obra social es obligatoria")
    @Size(max = 20, message = "La obra social no puede superar los 20 caracteres")
    @JsonProperty("obra_social")  // Mapea desde JSON con guión bajo
    private String obraSocial;
    
    @NotBlank(message = "El número de afiliado es obligatorio")
    @Size(max = 20, message = "El número de afiliado no puede superar los 20 caracteres")
    @JsonProperty("numero_afiliado")  // Mapea desde JSON con guión bajo
    private String numeroAfiliado;
}