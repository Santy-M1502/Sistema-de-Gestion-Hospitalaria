package com.SGH.hospital.dto.receta;

import java.time.LocalDate;

import com.SGH.hospital.enums.EstadoReceta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaResponse {
    
    private Long consultaId;
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmision;
    private EstadoReceta estadoReceta;

    public static RecetaResponse fromEntity(com.SGH.hospital.entity.Receta receta){
        return RecetaResponse.builder()
            .consultaId(receta.getConsulta().getId())
            .fechaVencimiento(receta.getFechaVencimiento())
            .fechaEmision(receta.getFechaEmision())
            .estadoReceta(receta.getEstadoReceta())
            .build();

            
    }
}
