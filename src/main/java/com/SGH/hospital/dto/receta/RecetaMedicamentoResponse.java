package com.SGH.hospital.dto.receta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaMedicamentoResponse {
    
    private Long id;
    private String medicamento;
    private String frecuencia;
    private String duracion;
    private String indicaciones;

    public static RecetaMedicamentoResponse fromEntity(com.SGH.hospital.entity.RecetaMedicamento receta){
        return RecetaMedicamentoResponse.builder()
            .id(receta.getId())
            .medicamento(receta.getMedicamento())
            .frecuencia(receta.getFrecuencia())
            .duracion(receta.getDuracion())
            .indicaciones(receta.getIndicaciones())
            .build();
    }
}
