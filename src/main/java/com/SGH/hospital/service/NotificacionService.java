package com.SGH.hospital.service;

import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.enums.TipoNotificacion;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviar(Turno turno, TipoNotificacion tipo) {

        String destino = turno.getPaciente().getEmail();
        String asunto;
        String mensaje;

        switch (tipo) {

            case CONFIRMACION_TURNO:
                asunto = "Confirmación de turno";
                mensaje = "Tu turno fue confirmado.\n" + info(turno);
                break;

            case RECORDATORIO_24HS:
                asunto = "Recordatorio de turno (24 hs antes)";
                mensaje = "Recordatorio: tenés un turno mañana.\n" + info(turno);
                break;

            case RECORDATORIO_2HS:
                asunto = "Recordatorio de turno (2 hs antes)";
                mensaje = "Recordatorio: tu turno es dentro de 2 horas.\n" + info(turno);
                break;

            case CANCELACION_TURNO:
                asunto = "Tu turno fue cancelado";
                mensaje = "El turno fue cancelado por el sistema o por el paciente.\n" + info(turno);
                break;

            case TURNO_DISPONIBLE:
                asunto = "Turno disponible";
                mensaje = "Se liberó un turno y fue asignado automáticamente.\n" + info(turno);
                break;

            default:
                throw new IllegalArgumentException("Tipo de notificación no válido");
        }

        enviarEmail(destino, asunto, mensaje);
    }

    private String info(Turno turno) {
        return "Médico: " + turno.getMedico().getNombre() +
                "\nEspecialidad: " + turno.getMedico() +
                "\nFecha: " + turno.getFecha() +
                "\nHora: " + turno.getHora();
    }

    // Simulación temporal
    private void enviarEmail(String destino, String asunto, String mensaje) {
        System.out.println("==================================");
        System.out.println("Email a: " + destino);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje:\n" + mensaje);
        System.out.println("==================================");
    }
}