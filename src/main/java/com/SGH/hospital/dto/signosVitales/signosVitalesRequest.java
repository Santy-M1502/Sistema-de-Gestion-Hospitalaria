package com.SGH.hospital.dto.signosVitales;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class signosVitalesRequest {

    @NotBlank(message = "La presión arterial es obligatoria")
    @Pattern(
        regexp = "^\\d{2,3}/\\d{2,3}$",
        message = "Formato inválido. Ej: 120/80"
    )
    private String presionArterial;

    @NotNull(message = "La frecuencia cardíaca es obligatoria")
    @Min(value = 30, message = "Frecuencia cardíaca inválida")
    @Max(value = 220, message = "Frecuencia cardíaca inválida")
    private Integer frecuenciaCardiaca;

    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "30.0", message = "Temperatura inválida")
    @DecimalMax(value = "45.0", message = "Temperatura inválida")
    private Double temperatura;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "1.0", message = "Peso inválido")
    private Double peso;

    @NotNull(message = "La altura es obligatoria")
    @DecimalMin(value = "0.5", message = "Altura inválida")
    @DecimalMax(value = "2.5", message = "Altura inválida")
    private Double altura;

    @NotNull(message = "La saturación de oxígeno es obligatoria")
    @Min(value = 50, message = "Saturación inválida")
    @Max(value = 100, message = "Saturación inválida")
    private Integer saturacionOxigeno;
}