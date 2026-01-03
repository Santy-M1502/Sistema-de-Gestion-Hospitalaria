package com.SGH.hospital.dto.auth;

// Enum que indica el rol del usuario dentro del sistema
import com.SGH.hospital.enums.Rol;

// Enum que indica el estado del usuario (ACTIVO, INACTIVO, etc.)
import com.SGH.hospital.enums.EstadoUsuario;

// Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

// Lombok: permite usar el patrón Builder
import lombok.Builder;

// Lombok: genera constructor vacío
import lombok.NoArgsConstructor;

// Lombok: genera constructor con todos los campos
import lombok.AllArgsConstructor;

// Tipo de dato para manejar fechas sin hora
import java.time.LocalDate;

// DTO usado para devolver información completa del usuario
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    // ID único del usuario
    private Long id;

    // Nombre del usuario
    private String nombre;

    // Apellido del usuario
    private String apellido;

    // Email del usuario
    private String email;

    // DNI del usuario
    private String dni;

    // Teléfono del usuario
    private String telefono;

    // Dirección del usuario
    private String direccion;

    // Fecha de nacimiento del usuario
    private LocalDate fechaNacimiento;

    // Rol del usuario dentro del sistema
    private Rol rol;

    // Estado actual del usuario en el sistema
    private EstadoUsuario estado;
}
