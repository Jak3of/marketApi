package com.api.market.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que valida el token JWT en cada petición
 * Se ejecuta una vez por cada petición HTTP (excepto /login)
 * 
 * Flujo de autorización:
 * 1. Cliente envía petición con token JWT en header Authorization
 * 2. Este filtro intercepta la petición
 * 3. Extrae y valida el token
 * 4. Si el token es válido, establece la autenticación en el SecurityContext
 * 5. Si no hay token o es inválido, deja la petición sin autenticación
 */
@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    /**
     * Método principal del filtro que se ejecuta en cada petición
     * 
     * @param request Petición HTTP entrante
     * @param response Respuesta HTTP
     * @param filterChain Cadena de filtros de seguridad
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        // Extrae el token del header Authorization
        String bearerToken = request.getHeader("Authorization");

        // Verifica si existe el token y tiene el formato correcto
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Extrae el token sin el prefijo "Bearer "
            String token = bearerToken.replace("Bearer ", "");
            
            // Valida el token y obtiene la autenticación
            UsernamePasswordAuthenticationToken usernamePAT = tokenUtils.getAuthentication(token);
            
            // Si el token es válido, establece la autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);
        }
        
        // Continúa con la cadena de filtros
        // Si no había token o era inválido, la petición continúa sin autenticación
        filterChain.doFilter(request, response);
    }
}
