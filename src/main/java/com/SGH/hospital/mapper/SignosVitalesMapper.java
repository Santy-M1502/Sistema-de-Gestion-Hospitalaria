package com.SGH.hospital.mapper;

import com.SGH.hospital.dto.signosVitales.signosVitalesRequest;
import com.SGH.hospital.dto.signosVitales.signosVitalesResponse;
import com.SGH.hospital.entity.SignosVitales;
import org.springframework.stereotype.Component;

@Component
public class SignosVitalesMapper {

    public SignosVitales toEntity(signosVitalesRequest request) {
        SignosVitales sv = new SignosVitales();
        sv.setPresionArterial(request.getPresionArterial());
        sv.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());
        sv.setTemperatura(request.getTemperatura());
        sv.setPeso(request.getPeso());
        sv.setAltura(request.getAltura());
        sv.setSaturacionOxigeno(request.getSaturacionOxigeno());
        return sv;
    }

    public signosVitalesResponse toResponse(SignosVitales sv) {
        return signosVitalesResponse.builder()
                .id(sv.getId())
                .presionArterial(sv.getPresionArterial())
                .frecuenciaCardiaca(sv.getFrecuenciaCardiaca())
                .temperatura(sv.getTemperatura())
                .peso(sv.getPeso())
                .altura(sv.getAltura())
                .imc(sv.getImc())
                .saturacionOxigeno(sv.getSaturacionOxigeno())
                .build();
    }
}