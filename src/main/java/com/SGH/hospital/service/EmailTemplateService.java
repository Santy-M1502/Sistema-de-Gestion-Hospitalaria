package com.SGH.hospital.service;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.SGH.hospital.dto.turno.TurnoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {
    private final TemplateEngine templateEngine;

    public String procesarConfirmacion(TurnoDTO data) {
        Context context = baseContext(data);
            context.setVariable("especialidad", data.getMedicoEspecialidades());
            context.setVariable("direccion", data.getPacienteDireccion());
        return templateEngine.process("email/confirmacion", context);
    }

    public String procesarRecordatorio24h(TurnoDTO data) {
        Context context = baseContext(data);
        return templateEngine.process("email/recordatorio-24h", context);
    }

    public String procesarRecordatorio2h(TurnoDTO data) {
        Context context = baseContext(data);
        return templateEngine.process("email/recordatorio-2h", context);
    }

    private Context baseContext(TurnoDTO data) {
        Context context = new Context();
        context.setVariable("nombrePaciente", data.getPacienteNombre());
        context.setVariable("nombreMedico", data.getMedicoNombre());
        context.setVariable("fecha", data.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("hora", data.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
        String linkCancelar = "https://http://localhost:8089/turnos/" + data.getId() + "/cancelar/";
        context.setVariable("linkCancelar", linkCancelar);
        return context;
    }
}
