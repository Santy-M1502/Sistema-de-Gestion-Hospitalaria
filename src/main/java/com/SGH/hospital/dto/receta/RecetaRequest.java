package com.SGH.hospital.dto.receta;

import java.time.LocalDate;
import java.util.List;

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

    @NotEmpty(message = "Debe incluir al menos un medicamento")
    @Valid
    private List<RecetaMedicamentoRequest> medicamentos;
}
