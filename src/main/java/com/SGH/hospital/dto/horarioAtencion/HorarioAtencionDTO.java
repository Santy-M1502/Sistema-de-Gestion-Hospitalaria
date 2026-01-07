package com.SGH.hospital.dto.horarioAtencion;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioAtencionDTO {
    
    private Long id;
    
    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeek diaSemana;
    
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
    
    private Boolean activo = true;

    public HorarioAtencionDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DayOfWeek getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DayOfWeek diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}