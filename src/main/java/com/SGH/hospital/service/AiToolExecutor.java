package com.SGH.hospital.service;

import com.SGH.hospital.dto.chat.AiToolCall;
import com.SGH.hospital.dto.chat.AiToolResult;
import com.SGH.hospital.entity.Medico;
import com.SGH.hospital.entity.Turno;
import com.SGH.hospital.repository.MedicoRepository;
import com.SGH.hospital.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AiToolExecutor - Ejecuta las acciones que la IA solicita
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiToolExecutor {

    private final TurnoService turnoService;
    private final MedicoRepository medicoRepository;
    private final TurnoRepository turnoRepository;

    /**
     * Ejecuta tool calls con transacción independiente (REQUIRES_NEW)
     * Esto permite escribir a BD incluso cuando es llamado desde un servicio read-only
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AiToolResult ejecutar(Long pacienteId, AiToolCall toolCall) {
        if (toolCall == null || !toolCall.isValid()) {
            return AiToolResult.error("UNKNOWN", "Tool call inválido");
        }

        try {
            return switch (toolCall.getTool().toUpperCase()) {
                case "OBTENER_DISPONIBILIDAD_TURNOS" -> obtenerDisponibilidadTurnos(toolCall);
                case "AGENDAR_TURNO" -> agendarTurno(pacienteId, toolCall);
                case "LISTAR_TURNOS_PACIENTE" -> listarTurnosPaciente(pacienteId, toolCall);
                case "LISTAR_MEDICOS" -> listarMedicos(toolCall);
                case "CANCELAR_TURNO" -> cancelarTurno(pacienteId, toolCall);
                default -> AiToolResult.error(toolCall.getTool(), "Tool no reconocida");
            };
        } catch (Exception ex) {
            log.error("[TOOL-EXECUTOR] Error ejecutando {}: {}", toolCall.getTool(), ex.getMessage(), ex);
            return AiToolResult.error(toolCall.getTool(), "Error: " + ex.getMessage());
        }
    }

    private AiToolResult obtenerDisponibilidadTurnos(AiToolCall toolCall) {
        try {
            Long medicoId = toolCall.getParamAsLong("medicoId");
            String fechaStr = toolCall.getParamAsString("fecha");

            if (medicoId == null) {
                return AiToolResult.error(toolCall.getTool(), "Parámetro 'medicoId' requerido");
            }

            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

            if (fechaStr != null) {
                LocalDate fecha = LocalDate.parse(fechaStr);
                List<LocalDateTime> disponibles = generarSlots(fecha, medico);
                String resultado = "Disponibilidad para " + medico.getNombre() + " el " + fecha + ":\n" +
                        disponibles.stream()
                                .map(dt -> dt.toLocalTime().toString())
                                .collect(Collectors.joining(", "));
                return AiToolResult.success(toolCall.getTool(), resultado);
            } else {
                LocalDate hoy = LocalDate.now();
                YearMonth mes = YearMonth.from(hoy);
                StringBuilder sb = new StringBuilder("Disponibilidad para ")
                        .append(medico.getNombre()).append(" en ").append(mes).append(":\n");

                for (int dia = hoy.getDayOfMonth(); dia <= mes.lengthOfMonth(); dia++) {
                    LocalDate fecha = LocalDate.of(mes.getYear(), mes.getMonth(), dia);
                    List<LocalDateTime> slots = generarSlots(fecha, medico);
                    if (!slots.isEmpty()) {
                        sb.append(String.format("%02d: %d slots\n", dia, slots.size()));
                    }
                }
                return AiToolResult.success(toolCall.getTool(), sb.toString());
            }
        } catch (Exception ex) {
            return AiToolResult.error(toolCall.getTool(), "Error: " + ex.getMessage());
        }
    }

    private List<LocalDateTime> generarSlots(LocalDate fecha, Medico medico) {
        List<Turno> turnosDelDia = turnoRepository.findByMedicoIdAndFechaBetween(
                medico.getId(),
                fecha.atStartOfDay(),
                fecha.atTime(23, 59, 59)
        );

        List<LocalDateTime> slots = new java.util.ArrayList<>();
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(17, 0);

        for (LocalTime hora = inicio; hora.isBefore(fin); hora = hora.plusMinutes(30)) {
            LocalDateTime slot = fecha.atTime(hora);
            final LocalTime horaFinal = hora;  // Capturar para usar en lambda
            boolean ocupado = turnosDelDia.stream()
                    .anyMatch(t -> t.getHora().getHour() == horaFinal.getHour() &&
                            t.getHora().getMinute() == horaFinal.getMinute());
            if (!ocupado) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private AiToolResult agendarTurno(Long pacienteId, AiToolCall toolCall) {
        try {
            Long medicoId = toolCall.getParamAsLong("medicoId");
            String fechaStr = toolCall.getParamAsString("fecha");
            String horaStr = toolCall.getParamAsString("hora");
            String motivo = toolCall.getParamAsString("motivo");

            if (medicoId == null || fechaStr == null || horaStr == null) {
                return AiToolResult.error(toolCall.getTool(), "Parámetros requeridos: medicoId, fecha, hora");
            }

            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime dateTime = fecha.atTime(hora);

            Turno turno = turnoService.agendarTurno(pacienteId, medicoId, fecha, dateTime,
                    motivo != null ? motivo : "Consulta general");

            String resultado = String.format("✓ Turno agendado para %s a las %s con Dr./Dra. %s",
                    fecha, hora, turno.getMedico().getNombre());

            return AiToolResult.success(toolCall.getTool(), resultado);
        } catch (Exception ex) {
            return AiToolResult.error(toolCall.getTool(), "Error: " + ex.getMessage());
        }
    }

    private AiToolResult listarTurnosPaciente(Long pacienteId, AiToolCall toolCall) {
        try {
            List<Turno> turnos = turnoRepository.findByPacienteId(pacienteId);
            
            // Aplicar defaults inteligentes
            Boolean auxIncluirAusentes = toolCall != null ? toolCall.getParamAsBoolean("incluirAusentes") : null;
            Boolean auxSoloFuturos = toolCall != null ? toolCall.getParamAsBoolean("soloFuturos") : null;
            
            // Defaults: no mostrar ausentes, solo futuros
            final boolean incluirAusentes = auxIncluirAusentes != null ? auxIncluirAusentes : false;
            final boolean soloFuturos = auxSoloFuturos != null ? auxSoloFuturos : true;

            // Filtrar según parámetros
            LocalDateTime ahora = LocalDateTime.now();
            List<Turno> turnosFiltrados = turnos.stream()
                    .filter(t -> {
                        // Filtrar ausentes si no se piden
                        if (!incluirAusentes && t.getEstado() != null && 
                            t.getEstado().toString().equalsIgnoreCase("AUSENTE")) {
                            return false;
                        }
                        // Filtrar pasados si solo se piden futuros
                        if (soloFuturos && t.getHora() != null && t.getHora().isBefore(ahora)) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            if (turnosFiltrados.isEmpty()) {
                String msg = soloFuturos ? "No tienes turnos próximos" : "No tienes turnos agendados";
                return AiToolResult.success("LISTAR_TURNOS_PACIENTE", msg);
            }

            StringBuilder sb = new StringBuilder("Tus turnos:\n");
            for (Turno turno : turnosFiltrados) {
                sb.append(String.format("- %s a las %s con Dr./Dra. %s (%s)\n",
                        turno.getFecha(),
                        turno.getHora().toLocalTime(),
                        turno.getMedico().getNombre(),
                        turno.getEstado()));
            }
            return AiToolResult.success("LISTAR_TURNOS_PACIENTE", sb.toString());
        } catch (Exception ex) {
            return AiToolResult.error("LISTAR_TURNOS_PACIENTE", "Error: " + ex.getMessage());
        }
    }

    private AiToolResult listarMedicos(AiToolCall toolCall) {
        try {
            List<Medico> medicos = medicoRepository.findAll();
            
            // Aplicar defaults inteligentes
            final String especialidad = toolCall != null ? toolCall.getParamAsString("especialidad") : null;
            Boolean auxSoloDisponibles = toolCall != null ? toolCall.getParamAsBoolean("soloDisponibles") : null;
            
            // Default: no filtrar solo disponibles
            final boolean soloDisponibles = auxSoloDisponibles != null ? auxSoloDisponibles : false;

            // Filtrar según parámetros
            List<Medico> medicosFiltrados = medicos.stream()
                    .filter(m -> {
                        // Filtrar por especialidad si se especifica
                        if (especialidad != null && !especialidad.isEmpty()) {
                            boolean tieneEspecialidad = m.getEspecialidades() != null &&
                                    m.getEspecialidades().stream()
                                            .anyMatch(e -> e.getNombre().toLowerCase()
                                                    .contains(especialidad.toLowerCase()));
                            if (!tieneEspecialidad) return false;
                        }
                        // Filtrar solo disponibles si se pide
                        if (soloDisponibles && (m.getDisponible() == null || !m.getDisponible())) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            if (medicosFiltrados.isEmpty()) {
                String msg = especialidad != null 
                    ? "No hay médicos de " + especialidad + " disponibles"
                    : "No hay médicos disponibles";
                return AiToolResult.success("LISTAR_MEDICOS", msg);
            }

            StringBuilder sb = new StringBuilder();
            if (especialidad != null) {
                sb.append("Médicos de ").append(especialidad).append(":\n");
            } else {
                sb.append("Médicos disponibles:\n");
            }

            for (Medico medico : medicosFiltrados) {
                String especialidades = medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty()
                        ? medico.getEspecialidades().stream()
                        .map(e -> e.getNombre())
                        .collect(Collectors.joining(", "))
                        : "Sin especialidad";

                sb.append(String.format("- %s (%s) - ID: %d\n",
                        medico.getNombre(),
                        especialidades,
                        medico.getId()));
            }
            return AiToolResult.success("LISTAR_MEDICOS", sb.toString());
        } catch (Exception ex) {
            return AiToolResult.error("LISTAR_MEDICOS", "Error: " + ex.getMessage());
        }
    }

    private AiToolResult cancelarTurno(Long pacienteId, AiToolCall toolCall) {
        try {
            Long turnoId = toolCall.getParamAsLong("turnoId");

            if (turnoId == null) {
                return AiToolResult.error(toolCall.getTool(), "Parámetro 'turnoId' requerido");
            }

            Turno turno = turnoRepository.findById(turnoId)
                    .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

            if (!turno.getPaciente().getId().equals(pacienteId)) {
                return AiToolResult.error(toolCall.getTool(), "No puedes cancelar un turno que no es tuyo");
            }

            turnoService.cancelarTurno(turnoId);
            return AiToolResult.success(toolCall.getTool(), "Turno cancelado exitosamente");
        } catch (Exception ex) {
            return AiToolResult.error(toolCall.getTool(), "Error: " + ex.getMessage());
        }
    }
}
