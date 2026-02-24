package com.SGH.hospital.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.SGH.hospital.dto.turno.TurnoDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    @Value("${spring.mail.username}")
    private String emailOrigen;

    @Async("emailExecutor")
    public void enviarConfirmacion(TurnoDTO data){
        String html = templateService.procesarConfirmacion(data);
        enviar(data.getPacienteEmail(), "Tu turno fue confirmado", html);
    }

    @Async("emailExecutor")
    public void enviarRecordatorio24h(TurnoDTO data) {
        String html = templateService.procesarRecordatorio24h(data);
        enviar(data.getPacienteEmail(), "Recordatorio: tu turno es mañana", html);
    }

    @Async("emailExecutor")
    public void enviarRecordatorio2h(TurnoDTO data) {
        String html = templateService.procesarRecordatorio2h(data);
        enviar(data.getPacienteEmail(), "Recordatorio: tu turno es en 2 horas", html);
    }

    @Async("emailExecutor")
    public void enviarRecordatorioMedico(TurnoDTO data){
        String html = templateService.procesarRecordatorioMedico(data);
        enviar(data.getMedicoEmail(), "Recordatorio: tienes un turno en las siguientes 24 horas", html);
    }

    private void enviar(String para, String asunto, String htmlContent) {
        try {
            // MimeMessage es el email en formato MIME (soporta HTML, adjuntos, etc)
            // SimpleMailMessage solo soporta texto plano, no sirve para HTML
            MimeMessage message = mailSender.createMimeMessage();

            // MimeMessageHelper es el wrapper cómodo para construir el MimeMessage
            // true = multipart (necesario para HTML)
            // "UTF-8" = para que funcionen tildes y ñ
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailOrigen);
            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(htmlContent, true); // el segundo true indica que es HTML

            // acá es donde realmente se abre la conexión TCP al servidor SMTP
            // se autentica, transfiere el email y cierra la conexión
            mailSender.send(message);

            log.info("Email enviado a {} | asunto: {}", para, asunto);

        } catch (MessagingException e) {
            // no relanzás la excepción para no romper el flujo principal de la app
            // el turno ya se guardó en la BD, el email fallido no debería revertir eso
            log.error("Falló el envío a {} | error: {}", para, e.getMessage());
        }
    }
}
