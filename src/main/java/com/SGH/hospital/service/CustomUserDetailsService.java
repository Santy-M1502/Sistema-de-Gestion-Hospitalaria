package com.SGH.hospital.service;

// Entidad Usuario del sistema
import com.SGH.hospital.entity.Usuario;

// Repositorio para acceder a usuarios en la base de datos
import com.SGH.hospital.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

// Clases de Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Anotaciones de Spring
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

// Marca esta clase como un Service de Spring
@Service

// Lombok genera un constructor con los atributos final
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Repositorio para buscar usuarios por email
    private final UsuarioRepository usuarioRepository;

    // Método que Spring Security usa para cargar el usuario al hacer login
    @Override
    @Transactional(readOnly = true) // Solo lectura, no modifica la BD
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca el usuario por email en la base de datos
        // Si no existe, lanza una excepción
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado con email: " + email)
                );

        // Construye el objeto User que Spring Security entiende
        return User.builder()
                // Email usado como username
                .username(usuario.getEmail())

                // Contraseña encriptada
                .password(usuario.getPassword())

                // Rol del usuario, con prefijo ROLE_ (obligatorio en Spring Security)
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                ))

                // La cuenta no está expirada
                .accountExpired(false)

                // La cuenta se bloquea si el estado NO es ACTIVO
                .accountLocked(usuario.getEstado() != com.SGH.hospital.enums.EstadoUsuario.ACTIVO)

                // Las credenciales no están expiradas
                .credentialsExpired(false)

                // El usuario se deshabilita si está INACTIVO
                .disabled(usuario.getEstado() == com.SGH.hospital.enums.EstadoUsuario.INACTIVO)

                // Devuelve el UserDetails final
                .build();
    }
}
