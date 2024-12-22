package com.api.market.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilidad para manejar tokens JWT
 */
@Component
public class TokenUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    /**
     * Crea un token JWT con los datos del usuario y sus roles
     */
    public String createToken(String name, String email, Collection<String> roles) {
        long tokenExpiration = expirationTime * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() + tokenExpiration);

        Map<String, Object> extra = new HashMap<>();
        extra.put("name", name);
        extra.put("roles", roles);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    /**
     * Valida un token JWT y extrae la informaci n de autenticacion
     * 
     * Si el token es v lido, devuelve un objeto UsernamePasswordAuthenticationToken
     * con el email del usuario y sus roles
     * 
     * Si el token es inv lido, devuelve null
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            // Crea un parser para el token con la clave secreta
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extrae el email del usuario del token
            String email = claims.getSubject();
            
            // Extrae los roles del usuario del token
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");

            // Crea una lista de roles como objetos SimpleGrantedAuthority
            Collection<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Crea un objeto UsernamePasswordAuthenticationToken con el email y los roles
            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (JwtException e) {
            // Si el token es inv lido, devuelve null
            return null;
        }
    }
}
