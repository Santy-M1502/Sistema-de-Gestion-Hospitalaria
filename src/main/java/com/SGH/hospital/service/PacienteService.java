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
@Slf4j // habilita logs con log.info, log.error, etc
@Service // marca la clase como servicio de Spring
@RequiredArgsConstructor // inyecta dependencias finales por constructor
public class PacienteService {

    // Acceso a la base de datos de pacientes
    private final PacienteRepository pacienteRepository;

    // Se usa para encriptar la contraseña
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo paciente
     */
    @Transactional // operación de escritura en la base de datos
    public PacienteResponse crearPaciente(PacienteRequest request) {
        log.info("Creando paciente con DNI: {}", request.getDni());

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

        // Se cargan los datos heredados de Usuario
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDni(request.getDni());
        paciente.setEmail(request.getEmail());

        // Se encripta la contraseña antes de guardarla
        paciente.setPassword(passwordEncoder.encode(request.getPassword()));

        paciente.setTelefono(request.getTelefono());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setDireccion(request.getDireccion());

        // Se asigna rol y estado por defecto
        paciente.setRol(Rol.PACIENTE);
        paciente.setEstado(EstadoUsuario.ACTIVO);

        paciente.setObraSocial(request.getObraSocial());
        paciente.setNumeroAfiliado(request.getNumeroAfiliado());

        // Se guarda el paciente en la base de datos
        Paciente savedPaciente = pacienteRepository.save(paciente);

        log.info("Paciente creado exitosamente con ID: {}", savedPaciente.getId());

        // Se devuelve el paciente convertido a DTO de respuesta
        return PacienteResponse.fromEntity(savedPaciente);
    }

    /**
     * Obtiene un paciente por ID
     */
    @Transactional(readOnly = true) // solo lectura
    public PacienteResponse obtenerPorId(Long id) {
        log.info("Obteniendo paciente con ID: {}", id);

        // Busca el paciente o lanza excepción si no existe
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
        log.info(
            "Listando pacientes - página: {}, tamaño: {}",
            pageable.getPageNumber(),
            pageable.getPageSize()
        );

        // Convierte cada entidad Paciente a PacienteResponse
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

        // Busca el paciente o lanza excepción
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));

        // Si cambia el email, se valida que no esté en uso
        if (request.getEmail() != null && !request.getEmail().equals(paciente.getEmail())) {
            if (pacienteRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException("Paciente", "email", request.getEmail());
            }
            paciente.setEmail(request.getEmail());
        }

        // Actualiza solo los campos que vienen en el request
        Optional.ofNullable(request.getNombre()).ifPresent(paciente::setNombre);
        Optional.ofNullable(request.getApellido()).ifPresent(paciente::setApellido);
        Optional.ofNullable(request.getTelefono()).ifPresent(paciente::setTelefono);
        Optional.ofNullable(request.getFechaNacimiento()).ifPresent(paciente::setFechaNacimiento);
        Optional.ofNullable(request.getDireccion()).ifPresent(paciente::setDireccion);
        Optional.ofNullable(request.getObraSocial()).ifPresent(paciente::setObraSocial);
        Optional.ofNullable(request.getNumeroAfiliado()).ifPresent(paciente::setNumeroAfiliado);

        // Guarda los cambios
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

        // No se borra de la base, solo se marca como inactivo
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
