package com.SGH.hospital.service;

import com.SGH.hospital.dto.especialidad.EspecialidadDTO;
import com.SGH.hospital.dto.horarioAtencion.HorarioAtencionDTO;
import com.SGH.hospital.dto.medico.MedicoRequest;
import com.SGH.hospital.dto.medico.MedicoResponse;
import com.SGH.hospital.dto.medico.MedicoUpdateRequest;
import com.SGH.hospital.entity.*;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.enums.Rol;
import com.SGH.hospital.exception.EspecialidadNotFoundException;
import com.SGH.hospital.exception.MedicoNotFoundException;
import com.SGH.hospital.repository.EspecialidadRepository;
import com.SGH.hospital.repository.HorarioAtencionRepository;
import com.SGH.hospital.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicoService {

    private static final Logger log = LoggerFactory.getLogger(MedicoService.class);

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(MedicoRepository medicoRepository, 
                        EspecialidadRepository especialidadRepository,
                        HorarioAtencionRepository horarioRepository,
                        PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== CRUD Básico ====================

    public MedicoResponse crearMedico(MedicoRequest request) {
        log.info("Creando nuevo médico con matrícula: {}", request.getMatricula());

        // Validar duplicados
        if (medicoRepository.existsByMatricula(request.getMatricula())) {
            throw new IllegalArgumentException("Ya existe un médico con la matrícula: " + request.getMatricula());
        }
        if (medicoRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.getEmail());
        }
        if (medicoRepository.existsByDni(request.getDni())) {
            throw new IllegalArgumentException("Ya existe un médico con el DNI: " + request.getDni());
        }

        // Obtener especialidades
        Set<Especialidad> especialidades = obtenerEspecialidades(request.getEspecialidadIds());

        // Crear entidad
        Medico medico = new Medico();
        medico.setMatricula(request.getMatricula());
        medico.setNombre(request.getNombre());
        medico.setApellido(request.getApellido());
        medico.setDni(request.getDni());
        medico.setEmail(request.getEmail());
        medico.setPassword(passwordEncoder.encode(request.getPassword()));
        medico.setTelefono(request.getTelefono());
        medico.setDireccion(request.getDireccion());
        medico.setFechaNacimiento(request.getFechaNacimiento());
        medico.setEspecialidades(especialidades);
        medico.setEstado(EstadoUsuario.ACTIVO);
        medico.setRol(Rol.MEDICO);

        // Guardar horarios si existen
        if (request.getHorarios() != null && !request.getHorarios().isEmpty()) {
            Set<HorarioAtencion> horarios = request.getHorarios().stream()
                    .map(dto -> crearHorario(dto, medico))
                    .collect(Collectors.toSet());
            medico.setHorariosAtencion(horarios);
        }

        Medico medicoGuardado = medicoRepository.save(medico);
        log.info("Médico creado exitosamente con ID: {}", medicoGuardado.getId());

        return convertirAResponse(medicoGuardado);
    }

    @Transactional(readOnly = true)
    public MedicoResponse obtenerPorId(Long id) {
        Medico medico = medicoRepository.findByIdWithAll(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + id));
        return convertirAResponse(medico);
    }

    @Transactional(readOnly = true)
    public Page<MedicoResponse> listarTodos(Pageable pageable) {
        return medicoRepository.findAll(pageable)
                .map(this::convertirAResponse);
    }

    public MedicoResponse actualizarMedico(Long id, MedicoUpdateRequest request) {
        log.info("Actualizando médico ID: {}", id);

        Medico medico = medicoRepository.findByIdWithAll(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + id));

        // Actualizar campos opcionales
        if (request.getNombre() != null) {
            medico.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            medico.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null) {
            medico.setTelefono(request.getTelefono());
        }
        if (request.getDireccion() != null) {
            medico.setDireccion(request.getDireccion());
        }
        if (request.getAniosExperiencia() != null) {
            medico.setAniosExperiencia(request.getAniosExperiencia());
        }
        if (request.getBiografia() != null) {
            medico.setBiografia(request.getBiografia());
        }
        if (request.getDisponible() != null) {
            medico.setDisponible(request.getDisponible());
        }

        // Actualizar especialidades si se proporcionan
        if (request.getEspecialidadIds() != null && !request.getEspecialidadIds().isEmpty()) {
            Set<Especialidad> nuevasEspecialidades = obtenerEspecialidades(request.getEspecialidadIds());
            medico.getEspecialidades().clear();
            medico.getEspecialidades().addAll(nuevasEspecialidades);
        }

        Medico medicoActualizado = medicoRepository.save(medico);
        log.info("Médico actualizado exitosamente ID: {}", id);

        return convertirAResponse(medicoActualizado);
    }

    public void cambiarEstado(Long id, EstadoUsuario nuevoEstado) {
        log.info("Cambiando estado del médico ID: {} a {}", id, nuevoEstado);

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + id));

        medico.setEstado(nuevoEstado);
        medicoRepository.save(medico);

        log.info("Estado cambiado exitosamente");
    }

    // ==================== Especialidades ====================

    public MedicoResponse asignarEspecialidades(Long medicoId, Set<Long> especialidadIds) {
        log.info("Asignando especialidades al médico ID: {}", medicoId);

        Medico medico = medicoRepository.findByIdWithEspecialidades(medicoId)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + medicoId));

        Set<Especialidad> especialidades = obtenerEspecialidades(especialidadIds);
        
        medico.getEspecialidades().clear();
        medico.getEspecialidades().addAll(especialidades);

        Medico medicoActualizado = medicoRepository.save(medico);
        return convertirAResponse(medicoActualizado);
    }

    public MedicoResponse agregarEspecialidad(Long medicoId, Long especialidadId) {
        Medico medico = medicoRepository.findByIdWithEspecialidades(medicoId)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + medicoId));

        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + especialidadId));

        medico.addEspecialidad(especialidad);
        Medico medicoActualizado = medicoRepository.save(medico);

        return convertirAResponse(medicoActualizado);
    }

    public MedicoResponse removerEspecialidad(Long medicoId, Long especialidadId) {
        Medico medico = medicoRepository.findByIdWithEspecialidades(medicoId)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + medicoId));

        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + especialidadId));

        medico.removeEspecialidad(especialidad);
        Medico medicoActualizado = medicoRepository.save(medico);

        return convertirAResponse(medicoActualizado);
    }

    // ==================== Horarios ====================

    public MedicoResponse configurarHorarios(Long medicoId, Set<HorarioAtencionDTO> horariosDTO) {
        log.info("Configurando horarios para médico ID: {}", medicoId);

        Medico medico = medicoRepository.findByIdWithHorarios(medicoId)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + medicoId));

        // Eliminar horarios existentes
        medico.getHorariosAtencion().clear();

        // Agregar nuevos horarios
        Set<HorarioAtencion> nuevosHorarios = horariosDTO.stream()
                .map(dto -> crearHorario(dto, medico))
                .collect(Collectors.toSet());

        medico.getHorariosAtencion().addAll(nuevosHorarios);

        Medico medicoActualizado = medicoRepository.save(medico);
        return convertirAResponse(medicoActualizado);
    }

    public void cambiarDisponibilidad(Long id, Boolean disponible) {
        log.info("Cambiando disponibilidad del médico ID: {} a {}", id, disponible);

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico no encontrado con ID: " + id));

        medico.setDisponible(disponible);
        medicoRepository.save(medico);
    }

    // ==================== Búsquedas ====================

    @Transactional(readOnly = true)
    public Page<MedicoResponse> buscarPorEspecialidad(Long especialidadId, Pageable pageable) {
        return medicoRepository.findByEspecialidadId(especialidadId, pageable)
                .map(this::convertirAResponse);
    }

    @Transactional(readOnly = true)
    public Page<MedicoResponse> buscarDisponibles(Pageable pageable) {
        return medicoRepository.findByEspecialidadAndDisponibilidad(
                null, true, EstadoUsuario.ACTIVO, pageable
        ).map(this::convertirAResponse);
    }

    @Transactional(readOnly = true)
    public Page<MedicoResponse> buscarConFiltros(
            String nombre, String apellido, Long especialidadId, 
            Boolean disponible, EstadoUsuario estado, Pageable pageable) {
        
        return medicoRepository.buscarConFiltros(
                nombre, apellido, especialidadId, disponible, estado, pageable
        ).map(this::convertirAResponse);
    }

    // ==================== Métodos Auxiliares ====================

    private Set<Especialidad> obtenerEspecialidades(Set<Long> ids) {
        Set<Especialidad> especialidades = new HashSet<>();
        for (Long id : ids) {
            Especialidad especialidad = especialidadRepository.findById(id)
                    .orElseThrow(() -> new EspecialidadNotFoundException("Especialidad no encontrada con ID: " + id));
            especialidades.add(especialidad);
        }
        return especialidades;
    }

    private HorarioAtencion crearHorario(HorarioAtencionDTO dto, Medico medico) {
        HorarioAtencion horario = new HorarioAtencion();
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        horario.setMedico(medico);
        return horario;
    }

    private MedicoResponse convertirAResponse(Medico medico) {
        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setMatricula(medico.getMatricula());
        response.setNombre(medico.getNombre());
        response.setApellido(medico.getApellido());
        response.setDni(medico.getDni());
        response.setEmail(medico.getEmail());
        response.setTelefono(medico.getTelefono());
        response.setDireccion(medico.getDireccion());
        response.setFechaNacimiento(medico.getFechaNacimiento());
        response.setAniosExperiencia(medico.getAniosExperiencia());
        response.setBiografia(medico.getBiografia());
        response.setDisponible(medico.getDisponible());
        response.setEstado(medico.getEstado());
        response.setEspecialidades(convertirEspecialidades(medico.getEspecialidades()));
        response.setHorarios(convertirHorarios(medico.getHorariosAtencion()));
        response.setCreatedAt(medico.getCreatedAt());
        response.setUpdatedAt(medico.getUpdatedAt());
        return response;
    }

    private Set<EspecialidadDTO> convertirEspecialidades(Set<Especialidad> especialidades) {
        return especialidades.stream()
                .map(e -> {
                    EspecialidadDTO dto = new EspecialidadDTO();
                    dto.setId(e.getId());
                    dto.setNombre(e.getNombre());
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    private Set<HorarioAtencionDTO> convertirHorarios(Set<HorarioAtencion> horarios) {
        return horarios.stream()
                .map(h -> {
                    HorarioAtencionDTO dto = new HorarioAtencionDTO();
                    dto.setId(h.getId());
                    dto.setDiaSemana(h.getDiaSemana());
                    dto.setHoraInicio(h.getHoraInicio());
                    dto.setHoraFin(h.getHoraFin());
                    dto.setActivo(h.getActivo());
                    return dto;
                })
                .collect(Collectors.toSet());
    }
}