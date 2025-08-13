package com.example.demo.security;


import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
@Component
public class JwtUtil {

    // Clé secrète d'au moins 32 caractères (256 bits)
    private final String SECRET_KEY = "MaCleTresSecreteEtTresLonguePourJWT256Bits!123";

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 jour

    public String generateToken(String username , String role) {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role) // 👈 On ajoute le rôle ici

            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            // Utiliser Keys.hmacShaKeyFor pour créer une clé sécurisée
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

