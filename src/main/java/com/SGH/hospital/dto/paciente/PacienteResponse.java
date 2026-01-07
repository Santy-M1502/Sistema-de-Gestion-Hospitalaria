package com.SGH.hospital.dto.paciente;

import com.SGH.hospital.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String numeroAfiliado;
    private EstadoUsuario estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método estático para facilitar la conversión desde la entidad
    public static PacienteResponse fromEntity(com.SGH.hospital.entity.Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .dni(paciente.getDni())
                .email(paciente.getEmail())
                .telefono(paciente.getTelefono())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .direccion(paciente.getDireccion())
                .numeroAfiliado(paciente.getNumeroAfiliado())
                .estado(paciente.getEstado())
                .createdAt(paciente.getCreatedAt())
                .updatedAt(paciente.getUpdatedAt())
                .build();
    }
}