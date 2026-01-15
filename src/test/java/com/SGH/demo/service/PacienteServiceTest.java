package com.SGH.demo.service;

import com.SGH.hospital.dto.paciente.PacienteRequest;
import com.SGH.hospital.dto.paciente.PacienteResponse;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.exception.PacienteNotFoundException;
import com.SGH.hospital.repository.PacienteRepository;
import com.SGH.hospital.service.PacienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PacienteService
 * Usamos @ExtendWith(MockitoExtension.class) para activar Mockito
 */
@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock // Crea un mock (simulación) del repository
    private PacienteRepository pacienteRepository;

    @InjectMocks // Crea la instancia del service e inyecta los mocks
    private PacienteService pacienteService;

    private Paciente paciente;
    private PacienteRequest pacienteRequest;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba antes de cada test
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setEmail("juan@test.com");
        paciente.setDni("12345678");

        pacienteRequest = new PacienteRequest();
        pacienteRequest.setNombre("Juan");
        pacienteRequest.setApellido("Pérez");
        pacienteRequest.setEmail("juan@test.com");
        pacienteRequest.setDni("12345678");
    }

    @Test
    void crearPaciente_DeberiaRetornarPacienteCreado() {
        // ARRANGE (Preparar)
        // Configuramos qué debe devolver el mock cuando se llame al método save
        when(pacienteRepository.save(any(Paciente.class)))
                .thenReturn(paciente);

        // ACT (Actuar)
        PacienteResponse resultado = pacienteService.crearPaciente(pacienteRequest);

        // ASSERT (Verificar)
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan@test.com", resultado.getEmail());
        
        // Verificar que se llamó al método save exactamente 1 vez
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void obtenerPacientePorId_CuandoExiste_DeberiaRetornarPaciente() {
        // ARRANGE
        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        // ACT
        PacienteResponse resultado = pacienteService.obtenerPorId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPacientePorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // ARRANGE
        when(pacienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        // Verificamos que se lance la excepción esperada
        assertThrows(PacienteNotFoundException.class, () -> {
            pacienteService.obtenerPorId(999L);
        });
        
        verify(pacienteRepository, times(1)).findById(999L);
    }

    @Test
    void eliminarPaciente_DeberiaLlamarAlRepositorio() {
        // ARRANGE
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pacienteRepository).deleteById(1L);

        // ACT
        pacienteService.eliminarPaciente(1L);

        // ASSERT
        verify(pacienteRepository, times(1)).existsById(1L);
        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}