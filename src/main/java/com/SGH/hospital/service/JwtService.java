package com.SGH.hospital.service;

// Clases de la librería jjwt para manejar JWT
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// Java
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

// Marca esta clase como un Service de Spring
@Service
public class JwtService {

    // Clave secreta para firmar los tokens
    // Se obtiene desde application.properties o yml
    @Value("${jwt.secret}")
    private String secretKey;

    // Tiempo de expiración del token normal
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Tiempo de expiración del refresh token
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // Extrae el username (subject) desde el token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae un claim específico usando una función
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Genera un token JWT sin claims extra
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Genera un token JWT con claims extra
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Genera un refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    // Construye el token JWT
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                // Claims personalizados
                .setClaims(extraClaims)
                // Subject: normalmente el username o email
                .setSubject(userDetails.getUsername())
                // Fecha de creación del token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Fecha de expiración
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // Firma del token usando HMAC SHA256
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                // Construye el token final
                .compact();
    }

    // Verifica si el token es válido para un usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Verifica si el token ya expiró
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrae la fecha de expiración del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Convierte la secret key en una Key válida para JWT
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
