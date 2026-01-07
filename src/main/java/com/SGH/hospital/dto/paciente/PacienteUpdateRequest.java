package com.SGH.hospital.dto.paciente;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteUpdateRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @Email(message = "El email debe ser válido")
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "El teléfono debe tener entre 10 y 15 dígitos")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    private String direccion;

    @Size(max = 20, message = "El numero afiliado no puede superar los 20 caracteres")
    private String numeroAfiliado;

    @Size(max = 20, message = "La obra social no puede superar los 20 caracteres")
    private String obraSocial;
}