package com.SGH.hospital.mapper;

import org.springframework.stereotype.Component;

import com.SGH.hospital.dto.turno.TurnoDTO;
import com.SGH.hospital.entity.Turno;

@Component
public class TurnoMapper {
    public TurnoDTO toDTO(Turno turno){
        if(turno == null) return null;

        TurnoDTO dto = new TurnoDTO();

        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setHora(turno.getHora());
        dto.setEstado(turno.getEstado());
        dto.setMotivoConsulta(turno.getMotivo());

        dto.setPacienteId(turno.getPaciente().getId());
        dto.setPacienteNombre(turno.getPaciente().getNombre());
        dto.setPacienteApellido(turno.getPaciente().getApellido());
        dto.setPacienteEmail(turno.getPaciente().getEmail());

        dto.setMedicoId(turno.getMedico().getId());
        dto.setMedicoNombre(turno.getMedico().getNombre());
        dto.setMedicoApellido(turno.getMedico().getApellido());
        dto.setMedicoEmail(turno.getMedico().getEmail());

        dto.setRecordatorio24hEnviado(turno.isRecordatorio24hEnviado());
        dto.setRecordatorio2hEnviado(turno.isRecordatorio2hEnviado());

        return dto;
    }
}
