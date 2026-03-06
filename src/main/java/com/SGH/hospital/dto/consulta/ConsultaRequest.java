package com.SGH.hospital.dto.consulta;

import jakarta.validation.constraints.*;
import lombok.Data;

import com.SGH.hospital.dto.signosVitales.signosVitalesRequest;

@Data
public class ConsultaRequest {

    @NotNull(message = "El turno es obligatorio")
    private Long turnoId;

    @NotNull(message = "El médico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String diagnostico;
    private String observaciones;
    private String tratamiento;

    @NotNull(message = "Los signos vitales son obligatorios")
    private signosVitalesRequest signosVitales;
}