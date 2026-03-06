package com.SGH.hospital.dto.consulta;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import com.SGH.hospital.dto.signosVitales.signosVitalesResponse;

@Data
@Builder
public class ConsultaResponse {

    private Long id;

    private Long turnoId;
    private Long medicoId;

    private String nombreMedico;

    private LocalDateTime fecha;

    private String motivo;
    private String diagnostico;
    private String observaciones;
    private String tratamiento;

    private signosVitalesResponse signosVitales;
}