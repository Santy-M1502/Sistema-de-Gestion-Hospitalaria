package com.SGH.hospital.service;

import com.SGH.hospital.dto.paciente.PacienteRequest;
import com.SGH.hospital.dto.paciente.PacienteResponse;
import com.SGH.hospital.dto.paciente.PacienteUpdateRequest;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.enums.Rol;
import com.SGH.hospital.exception.DuplicateResourceException;
import com.SGH.hospital.exception.PacienteNotFoundException;
import com.SGH.hospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio que contiene toda la lógica de negocio relacionada a Paciente
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo paciente
     */
    @Transactional
    public PacienteResponse crearPaciente(PacienteRequest request) {
        log.info("========== INICIANDO CREACIÓN DE PACIENTE ==========");
        log.info("DNI: {}", request.getDni());
        
        // LOG: Verificar valores del REQUEST
        log.info("REQUEST - Obra Social: {}", request.getObraSocial());
        log.info("REQUEST - Numero Afiliado: {}", request.getNumeroAfiliado());
        log.info("REQUEST - Nombre: {}", request.getNombre());
        log.info("REQUEST - Email: {}", request.getEmail());

        // Verifica que no exista otro paciente con el mismo DNI
        if (pacienteRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Paciente", "DNI", request.getDni());
        }

        // Verifica que no exista otro paciente con el mismo email
        if (pacienteRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Paciente", "email", request.getEmail());
        }

        // Se crea la entidad Paciente vacía
        Paciente paciente = new Paciente();
        log.info("Paciente vacío creado");

        // Se cargan los datos heredados de Usuario
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDni(request.getDni());
        paciente.setEmail(request.getEmail());
        paciente.setPassword(passwordEncoder.encode(request.getPassword()));
        paciente.setTelefono(request.getTelefono());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setDireccion(request.getDireccion());
        paciente.setRol(Rol.PACIENTE);
        paciente.setEstado(EstadoUsuario.ACTIVO);

        log.info("Datos base del paciente asignados");

        // ANTES de asignar obra social y numero afiliado
        log.info("ANTES de setObraSocial - valor en request: {}", request.getObraSocial());
        log.info("ANTES de setNumeroAfiliado - valor en request: {}", request.getNumeroAfiliado());
        
        // Asignar obra social
        paciente.setObraSocial(request.getObraSocial());
        log.info("DESPUÉS de setObraSocial - valor en paciente: {}", paciente.getObraSocial());
        
        // Asignar numero afiliado
        paciente.setNumeroAfiliado(request.getNumeroAfiliado());
        log.info("DESPUÉS de setNumeroAfiliado - valor en paciente: {}", paciente.getNumeroAfiliado());

        // LOG: Verificar estado del objeto ANTES de guardar
        log.info("========== ESTADO DEL PACIENTE ANTES DE GUARDAR ==========");
        log.info("Paciente.obraSocial: {}", paciente.getObraSocial());
        log.info("Paciente.numeroAfiliado: {}", paciente.getNumeroAfiliado());
        log.info("Paciente.nombre: {}", paciente.getNombre());
        log.info("Paciente.email: {}", paciente.getEmail());

        // Intentar guardar
        try {
            log.info("Intentando guardar en base de datos...");
            Paciente savedPaciente = pacienteRepository.save(paciente);
            
            log.info("========== PACIENTE GUARDADO EXITOSAMENTE ==========");
            log.info("ID asignado: {}", savedPaciente.getId());
            log.info("Obra Social guardada: {}", savedPaciente.getObraSocial());
            log.info("Numero Afiliado guardado: {}", savedPaciente.getNumeroAfiliado());
            
            return PacienteResponse.fromEntity(savedPaciente);
            
        } catch (Exception e) {
            log.error("========== ERROR AL GUARDAR PACIENTE ==========");
            log.error("Tipo de excepción: {}", e.getClass().getName());
            log.error("Mensaje: {}", e.getMessage());
            log.error("Stack trace:", e);
            throw e;
        }
    }

    /**
     * Obtiene un paciente por ID
     */
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        log.info("Obteniendo paciente con ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
        return PacienteResponse.fromEntity(paciente);
    }

    /**
     * Obtiene un paciente por DNI
     */
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorDni(String dni) {
        log.info("Obteniendo paciente con DNI: {}", dni);
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNotFoundException("DNI", dni));
        return PacienteResponse.fromEntity(paciente);
    }

    /**
     * Obtiene un paciente por email
     */
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorEmail(String email) {
        log.info("Obteniendo paciente con email: {}", email);
        Paciente paciente = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new PacienteNotFoundException("email", email));
        return PacienteResponse.fromEntity(paciente);
    }

    /**
     * Lista todos los pacientes con paginación
     */
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarTodos(Pageable pageable) {
        log.info("Listando pacientes - página: {}, tamaño: {}", 
            pageable.getPageNumber(), pageable.getPageSize());
        return pacienteRepository.findAll(pageable)
                .map(PacienteResponse::fromEntity);
    }

    /**
     * Lista solo pacientes activos
     */
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarActivos(Pageable pageable) {
        log.info("Listando pacientes activos");
        return pacienteRepository.findAllActivos(pageable)
                .map(PacienteResponse::fromEntity);
    }

    /**
     * Lista pacientes filtrando por estado
     */
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarPorEstado(EstadoUsuario estado, Pageable pageable) {
        log.info("Listando pacientes con estado: {}", estado);
        return pacienteRepository.findByEstado(estado, pageable)
                .map(PacienteResponse::fromEntity);
    }

    /**
     * Busca pacientes por nombre o apellido
     */
    @Transactional(readOnly = true)
    public Page<PacienteResponse> buscarPorNombreOApellido(String searchTerm, Pageable pageable) {
        log.info("Buscando pacientes con término: {}", searchTerm);
        return pacienteRepository.searchByNombreOrApellido(searchTerm, pageable)
                .map(PacienteResponse::fromEntity);
    }

    /**
     * Actualiza los datos de un paciente existente
     */
    @Transactional
    public PacienteResponse actualizarPaciente(Long id, PacienteUpdateRequest request) {
        log.info("Actualizando paciente con ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));

        if (request.getEmail() != null && !request.getEmail().equals(paciente.getEmail())) {
            if (pacienteRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException("Paciente", "email", request.getEmail());
            }
            paciente.setEmail(request.getEmail());
        }

        Optional.ofNullable(request.getNombre()).ifPresent(paciente::setNombre);
        Optional.ofNullable(request.getApellido()).ifPresent(paciente::setApellido);
        Optional.ofNullable(request.getTelefono()).ifPresent(paciente::setTelefono);
        Optional.ofNullable(request.getFechaNacimiento()).ifPresent(paciente::setFechaNacimiento);
        Optional.ofNullable(request.getDireccion()).ifPresent(paciente::setDireccion);
        Optional.ofNullable(request.getObraSocial()).ifPresent(paciente::setObraSocial);
        Optional.ofNullable(request.getNumeroAfiliado()).ifPresent(paciente::setNumeroAfiliado);

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        log.info("Paciente actualizado exitosamente con ID: {}", id);
        return PacienteResponse.fromEntity(updatedPaciente);
    }

    /**
     * Cambia el estado de un paciente
     */
    @Transactional
    public PacienteResponse cambiarEstado(Long id, EstadoUsuario nuevoEstado) {
        log.info("Cambiando estado del paciente {} a {}", id, nuevoEstado);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
        paciente.setEstado(nuevoEstado);
        Paciente updatedPaciente = pacienteRepository.save(paciente);
        return PacienteResponse.fromEntity(updatedPaciente);
    }

    /**
     * Eliminación lógica del paciente
     */
    @Transactional
    public void eliminarPaciente(Long id) {
        log.info("Eliminando paciente con ID: {}", id);
        cambiarEstado(id, EstadoUsuario.INACTIVO);
    }

    /**
     * Cuenta pacientes según su estado
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoUsuario estado) {
        return pacienteRepository.countByEstado(estado);
    }
}