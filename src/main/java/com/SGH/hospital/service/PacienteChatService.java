package com.SGH.hospital.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SGH.hospital.chat.AiChatClient;
import com.SGH.hospital.dto.chat.AiChatResponse;
import com.SGH.hospital.dto.chat.ChatRequest;
import com.SGH.hospital.dto.chat.ChatResponse;
import com.SGH.hospital.entity.Antecedente;
import com.SGH.hospital.entity.HistoriaClinica;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.repository.HistoriaClinicaRepository;
import com.SGH.hospital.repository.PacienteRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
 
/**
 * PacienteChatService
 * ───────────────────
 * Orquesta la conversación entre el paciente y la IA.
 *
 * Su trabajo es:
 *  1. Verificar que el paciente exista en nuestra BD
 *  2. Construir un mensaje enriquecido con contexto clínico (si se pide)
 *  3. Delegar el HTTP al AiChatClient
 *  4. Devolver una respuesta limpia al controller
 *
 * ¿Por qué enriquecer el contexto aquí y no en el microservicio?
 * Porque el microservicio de IA es genérico: no conoce nuestra BD.
 * Nosotros sabemos quién es el paciente y podemos adjuntar su info
 * directamente en el primer mensaje de cada consulta.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteChatService {
 
    private final AiChatClient aiChatClient;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
 
    // ── Enviar mensaje ────────────────────────────────────────────────────────
 
    @Transactional(readOnly = true)
    public ChatResponse enviarMensaje(Long pacienteId, ChatRequest request) {
 
        // 1. Buscamos el paciente. Paciente extiende Usuario, así que
        //    getNombre(), getApellido(), getDni(), etc. vienen de allí.
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Paciente no encontrado con ID: " + pacienteId));
 
        // 2. Historia clínica — puede no existir todavía, está bien
        Optional<HistoriaClinica> historia =
                historiaClinicaRepository.findByPacienteId(pacienteId);
 
        // 3. sessionId = ID del paciente → cada paciente tiene su propio
        //    historial aislado en el microservicio de IA
        String sessionId = "paciente-" + pacienteId;
 
        // 4. Mensaje enriquecido con contexto clínico real
        String mensajeFinal = request.isIncluirContextoClinico()
                ? construirMensajeConContexto(paciente, historia.orElse(null), request.getMensaje())
                : request.getMensaje();
 
        log.info("[CHAT] Paciente {} ({}) → sesión '{}'",
                pacienteId, paciente.getNombre(), sessionId);
 
        // 5. Llamamos al microservicio de IA
        AiChatResponse aiResponse = aiChatClient.enviarMensaje(sessionId, mensajeFinal);
 
        // 6. Cuántos mensajes lleva la sesión (para mostrar en el frontend)
        int mensajesEnSesion = aiChatClient.obtenerCantidadMensajes(sessionId);
 
        // 7. Mapeamos al DTO del frontend
        if (aiResponse.tieneError()) {
            log.error("[CHAT] Error de IA para paciente {}: {}", pacienteId, aiResponse.getError());
            return ChatResponse.builder()
                    .error(true)
                    .mensajeError(aiResponse.getError())
                    .pacienteNombre(paciente.getNombre())
                    .mensajesEnSesion(mensajesEnSesion)
                    .build();
        }
 
        return ChatResponse.builder()
                .respuesta(aiResponse.getRespuesta())
                .modelo(aiResponse.getModelo())
                .tokensUsados(aiResponse.getTokensUsados())
                .tiempoMs(aiResponse.getTiempoMs())
                .pacienteNombre(paciente.getNombre() + " " + paciente.getApellido())
                .mensajesEnSesion(mensajesEnSesion)
                .error(false)
                .build();
    }
 
    // ── Limpiar historial ─────────────────────────────────────────────────────
 
    public void limpiarHistorial(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException("Paciente no encontrado con ID: " + pacienteId);
        }
        aiChatClient.limpiarSesion("paciente-" + pacienteId);
        log.info("[CHAT] Historial limpiado para paciente {}", pacienteId);
    }
 
    // ── Estado de la sesión ───────────────────────────────────────────────────
 
    public int obtenerEstadoSesion(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException("Paciente no encontrado con ID: " + pacienteId);
        }
        return aiChatClient.obtenerCantidadMensajes("paciente-" + pacienteId);
    }
 
    // ── Construcción del contexto clínico ─────────────────────────────────────
 
    /**
     * Prefija el mensaje del usuario con un bloque de contexto clínico.
     * El microservicio de IA ya tiene un system prompt hospitalario que
     * sabe interpretar este formato.
     *
     * Si historia == null (aún no fue creada), se omiten los datos clínicos
     * pero el método igual funciona sin lanzar excepción.
     */
    private String construirMensajeConContexto(Paciente paciente,
                                               HistoriaClinica historia,
                                               String mensajeUsuario) {
        StringJoiner ctx = new StringJoiner("\n");
 
        // ── Datos de Paciente (getters heredados de Usuario) ──────────────────
        ctx.add("[CONTEXTO DEL PACIENTE]");
        ctx.add("Nombre: " + paciente.getNombre() + " " + paciente.getApellido());
 
        // Ajustá getDni() si en tu Usuario se llama diferente
        if (paciente.getDni() != null) {
            ctx.add("DNI: " + paciente.getDni());
        }
 
        // Ajustá getFechaNacimiento() si está en Usuario con otro nombre
        if (paciente.getFechaNacimiento() != null) {
            int edad = Period.between(paciente.getFechaNacimiento(), LocalDate.now()).getYears();
            ctx.add("Edad: " + edad + " años");
        }
 
        // Campos propios de Paciente
        if (paciente.getObraSocial() != null && !paciente.getObraSocial().isBlank()) {
            ctx.add("Obra social: " + paciente.getObraSocial());
        }
        if (paciente.getNumeroAfiliado() != null && !paciente.getNumeroAfiliado().isBlank()) {
            ctx.add("Nro. afiliado: " + paciente.getNumeroAfiliado());
        }
 
        // ── Datos de HistoriaClinica ───────────────────────────────────────────
        if (historia != null) {
 
            if (historia.getGrupoSanguineo() != null) {
                // GrupoSanguineo es un enum → .name() devuelve "A_POSITIVO" etc.
                // Si tu enum tiene un campo descripcion o label, usalo en su lugar
                ctx.add("Grupo sanguíneo: " + historia.getGrupoSanguineo().name());
            }
 
            if (historia.getAlergias() != null && !historia.getAlergias().isBlank()) {
                ctx.add("Alergias: " + historia.getAlergias());
            }
 
            List<Antecedente> antecedentes = historia.getAntecedentes();
            if (antecedentes != null && !antecedentes.isEmpty()) {
                // Ajustá getDescripcion() al getter real de tu entidad Antecedente
                String lista = antecedentes.stream()
                        .map(Antecedente::getDescripcion)
                        .filter(d -> d != null && !d.isBlank())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
                if (!lista.isBlank()) {
                    ctx.add("Antecedentes: " + lista);
                }
            }
 
        } else {
            ctx.add("(Historia clínica aún no registrada)");
        }
 
        // ── Mensaje real del paciente ──────────────────────────────────────────
        ctx.add("[CONSULTA DEL PACIENTE]");
        ctx.add(mensajeUsuario);
 
        return ctx.toString();
    }
}