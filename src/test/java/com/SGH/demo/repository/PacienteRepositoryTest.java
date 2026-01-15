package com.SGH.demo.repository;

import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.repository.PacienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para PacienteRepository
 * @SpringBootTest levanta el contexto completo de Spring
 * @Transactional hace rollback después de cada test
 */
@SpringBootTest
@Transactional
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        // Limpiar la BD antes de cada test
        pacienteRepository.deleteAll();

        // Crear paciente de prueba
        paciente = new Paciente();
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setEmail("juan@test.com");
        paciente.setDni("12345678");
        paciente.setEstado(EstadoUsuario.ACTIVO);
        paciente.setPassword("hashedPassword");
    }

    @Test
    void save_DeberiaPersistirPaciente() {
        // ACT
        Paciente guardado = pacienteRepository.save(paciente);

        // ASSERT
        assertNotNull(guardado.getId());
        assertEquals("Juan", guardado.getNombre());
        assertEquals("juan@test.com", guardado.getEmail());
    }

    @Test
    void findByEmail_CuandoExiste_DeberiaRetornarPaciente() {
        // ARRANGE
        pacienteRepository.save(paciente);

        // ACT
        Optional<Paciente> encontrado = pacienteRepository.findByEmail("juan@test.com");

        // ASSERT
        assertTrue(encontrado.isPresent());
        assertEquals("Juan", encontrado.get().getNombre());
    }

    @Test
    void findByEmail_CuandoNoExiste_DeberiaRetornarEmpty() {
        // ACT
        Optional<Paciente> encontrado = pacienteRepository.findByEmail("noexiste@test.com");

        // ASSERT
        assertFalse(encontrado.isPresent());
    }

    @Test
    void findByDni_DeberiaRetornarPacienteCorrecto() {
        // ARRANGE
        pacienteRepository.save(paciente);

        // ACT
        Optional<Paciente> encontrado = pacienteRepository.findByDni("12345678");

        // ASSERT
        assertTrue(encontrado.isPresent());
        assertEquals("juan@test.com", encontrado.get().getEmail());
    }

    @Test
    void findByEstado_DeberiaRetornarSoloPacientesActivos() {
        // ARRANGE
        Paciente paciente2 = new Paciente();
        paciente2.setNombre("María");
        paciente2.setApellido("García");
        paciente2.setEmail("maria@test.com");
        paciente2.setDni("87654321");
        paciente2.setEstado(EstadoUsuario.INACTIVO);
        paciente2.setPassword("hashedPassword");

        pacienteRepository.save(paciente);
        pacienteRepository.save(paciente2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> activos = pacienteRepository.findByEstado(EstadoUsuario.ACTIVO, pageable);

        // ASSERT
        assertEquals(1, activos.getSize());
        assertEquals("Juan", activos.getContent().get(0).getNombre());
    }

    @Test
    void existsByEmail_CuandoExiste_DeberiaRetornarTrue() {
        // ARRANGE
        pacienteRepository.save(paciente);

        // ACT
        boolean existe = pacienteRepository.existsByEmail("juan@test.com");

        // ASSERT
        assertTrue(existe);
    }

    @Test
    void existsByEmail_CuandoNoExiste_DeberiaRetornarFalse() {
        // ACT
        boolean existe = pacienteRepository.existsByEmail("noexiste@test.com");

        // ASSERT
        assertFalse(existe);
    }

    @Test
    void deleteById_DeberiaEliminarPaciente() {
        // ARRANGE
        Paciente guardado = pacienteRepository.save(paciente);
        Long id = guardado.getId();

        // ACT
        pacienteRepository.deleteById(id);

        // ASSERT
        Optional<Paciente> eliminado = pacienteRepository.findById(id);
        assertFalse(eliminado.isPresent());
    }
}