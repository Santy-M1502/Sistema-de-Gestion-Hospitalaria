package com.SGH.hospital.dto.receta;

import java.time.LocalDate;

import com.SGH.hospital.enums.EstadoReceta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder    
@NoArgsConstructor
@AllArgsConstructor
public class RecetaRequest {

    @NotNull(message = "El ID de la consulta es obligatorio")
    private Long consultaId;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La fecha de vencimiento debe ser futura")
    private LocalDate fechaVencimiento;

    @NotEmpty(message = "La fecha de emision es obligatoria")
    @Valid
    private LocalDate fechaEmision;
    
    @NotEmpty(message = "El estado de receta es obligatorio")
    private EstadoReceta estadoReceta;
}
