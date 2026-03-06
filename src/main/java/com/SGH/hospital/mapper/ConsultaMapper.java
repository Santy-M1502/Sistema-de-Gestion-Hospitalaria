package com.SGH.hospital.mapper;

import com.SGH.hospital.dto.consulta.ConsultaResponse;
import com.SGH.hospital.dto.signosVitales.signosVitalesResponse;
import com.SGH.hospital.entity.Consulta;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public ConsultaResponse toResponse(Consulta consulta) {
        signosVitalesResponse svResponse = null;

        if (consulta.getSignosVitales() != null) {
            svResponse = signosVitalesResponse.builder()
                    .presionArterial(consulta.getSignosVitales().getPresionArterial())
                    .frecuenciaCardiaca(consulta.getSignosVitales().getFrecuenciaCardiaca())
                    .temperatura(consulta.getSignosVitales().getTemperatura())
                    .peso(consulta.getSignosVitales().getPeso())
                    .altura(consulta.getSignosVitales().getAltura())
                    .imc(consulta.getSignosVitales().getImc())
                    .saturacionOxigeno(consulta.getSignosVitales().getSaturacionOxigeno())
                    .build();
        }

        return ConsultaResponse.builder()
                .id(consulta.getId())
                .turnoId(consulta.getTurno().getId())
                .medicoId(consulta.getMedico().getId())
                .nombreMedico(consulta.getMedico().getNombre())
                .fecha(consulta.getFecha())
                .motivo(consulta.getMotivo())
                .diagnostico(consulta.getDiagnostico())
                .observaciones(consulta.getObservaciones())
                .tratamiento(consulta.getTratamiento())
                .signosVitales(svResponse)
                .build();
    }
}