package com.SGH.hospital.dto.signosVitales;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class signosVitalesResponse {

    private Long id;

    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private Double temperatura;
    private Double peso;
    private Double altura;
    private Double imc;
    private Integer saturacionOxigeno;
}