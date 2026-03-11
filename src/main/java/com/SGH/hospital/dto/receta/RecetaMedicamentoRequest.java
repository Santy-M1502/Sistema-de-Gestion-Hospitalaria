package com.SGH.hospital.dto.receta;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaMedicamentoRequest {
    
    @NotNull(message = "El medicamento de la receta es obligatorio")
    private String medicamento;

    @NotNull(message = "La dosis de la receta es obligatorio")
    private String dosis;

    @NotNull(message = "La frecuencia de la receta es obligatorio")
    private String frecuencia;

    @NotNull(message = "La duracion de la receta es obligatorio")
    private Long duracion;
}
