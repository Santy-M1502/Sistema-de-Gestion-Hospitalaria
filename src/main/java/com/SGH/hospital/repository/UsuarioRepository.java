package com.SGH.hospital.repository;

import com.SGH.hospital.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Encuentra usuario por Email
     * 
     * @param email
     * @return
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica que este registrado ese Email
     * 
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica que este registrado ese Dni
     * 
     * @param dni
     * @return
     */
    boolean existsByDni(String dni);
    
    /**
     * Encuentra usuario por Dni
     * 
     * @param dni
     * @return
     */
    Optional<Usuario> findByDni(String dni);
}