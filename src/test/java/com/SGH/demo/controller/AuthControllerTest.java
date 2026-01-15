package com.SGH.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SGH.hospital.dto.auth.LoginRequest;
import com.SGH.hospital.dto.auth.RegisterRequest;
import com.SGH.hospital.dto.auth.UserInfoResponse;
import com.SGH.hospital.dto.auth.AuthResponse;
import com.SGH.hospital.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Tests de integración para AuthController
 */
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void register_ConDatosValidos_DeberiaRetornar201() throws Exception {
        // ARRANGE
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@test.com");
        request.setPassword("Password123!");
        request.setDni("12345678");

        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token-123");
        response.setEmail("juan@test.com");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        // ACT & ASSERT
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void register_ConEmailInvalido_DeberiaRetornar400() throws Exception {
        // ARRANGE
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan");
        request.setEmail("email-invalido");
        request.setPassword("Password123!");

        // ACT & ASSERT
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ConCredencialesValidas_DeberiaRetornar200() throws Exception {
        // ARRANGE
        LoginRequest request = new LoginRequest();
        request.setEmail("juan@test.com");
        request.setPassword("Password123!");

        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token-456");
        response.setEmail("juan@test.com");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        // ACT & ASSERT
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    @WithMockUser(username = "juan@test.com")
    void obtenerUsuarioActual_ConUsuarioAutenticado_DeberiaRetornar200() throws Exception {
        // ARRANGE
        UserInfoResponse response = new UserInfoResponse();  // Cambio de AuthResponse a UserInfoResponse
        response.setEmail("juan@test.com");
        response.setNombre("Juan Pérez");

        when(authService.getCurrentUser())
                .thenReturn(response);

        // ACT & ASSERT
        mockMvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void obtenerUsuarioActual_SinAutenticacion_DeberiaRetornar401() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}