package com.SGH.hospital.service;

import com.SGH.hospital.dto.auth.*;
import com.SGH.hospital.enums.EstadoUsuario;
import com.SGH.hospital.entity.Paciente;
import com.SGH.hospital.enums.Rol;
import com.SGH.hospital.entity.Usuario;
import com.SGH.hospital.exception.BadRequestException;
import com.SGH.hospital.exception.ResourceNotFoundException;
import com.SGH.hospital.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }
        
        // Verificar si el DNI ya existe
        if (usuarioRepository.existsByDni(request.getDni())) {
            throw new BadRequestException("El DNI ya está registrado");
        }
        
        // Crear nuevo paciente
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setEmail(request.getEmail());
        paciente.setPassword(passwordEncoder.encode(request.getPassword()));
        paciente.setDni(request.getDni());
        paciente.setTelefono(request.getTelefono());
        paciente.setDireccion(request.getDireccion());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setRol(Rol.PACIENTE);
        paciente.setEstado(EstadoUsuario.ACTIVO);
        
        Usuario savedUsuario = usuarioRepository.save(paciente);
        
        // Generar tokens
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", savedUsuario.getRol().name());
        extraClaims.put("userId", savedUsuario.getId());
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUsuario.getEmail())
                .password(savedUsuario.getPassword())
                .roles(savedUsuario.getRol().name())
                .build();
        
        String jwtToken = jwtService.generateToken(extraClaims, userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .id(savedUsuario.getId())
                .email(savedUsuario.getEmail())
                .nombre(savedUsuario.getNombre())
                .apellido(savedUsuario.getApellido())
                .rol(savedUsuario.getRol())
                .build();
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // Buscar usuario
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Verificar estado del usuario
        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new BadRequestException("Usuario inactivo o suspendido");
        }
        
        // Generar tokens
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol().name());
        extraClaims.put("userId", usuario.getId());
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
        
        String jwtToken = jwtService.generateToken(extraClaims, userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .rol(usuario.getRol())
                .build();
    }
    
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail == null) {
            throw new BadRequestException("Refresh token inválido");
        }
        
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
        
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new BadRequestException("Refresh token expirado o inválido");
        }
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol().name());
        extraClaims.put("userId", usuario.getId());
        
        String newAccessToken = jwtService.generateToken(extraClaims, userDetails);
        
        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .rol(usuario.getRol())
                .build();
    }
    
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return UserInfoResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .dni(usuario.getDni())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .build();
    }
}