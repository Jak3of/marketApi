package com.api.market.security;

import com.api.market.bean.AuthCredentials;
import com.api.market.bean.TokenResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Filtro que maneja el proceso de autenticación (login)
 * Se activa automáticamente en la ruta /login (configurable)
 * 
 * Flujo de autenticación:
 * 1. Usuario envía POST a /login con credenciales
 * 2. Este filtro captura la petición
 * 3. Extrae y valida las credenciales
 * 4. Si son válidas, genera un token JWT
 * 5. Envía el token en el header de la respuesta
 */
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenUtils tokenUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Intenta autenticar al usuario con las credenciales proporcionadas
     * 
     * @param request Petición HTTP que contiene las credenciales
     * @param response Respuesta HTTP (no usado aquí)
     * @return Objeto Authentication con los detalles del usuario si la autenticación es exitosa
     * @throws AuthenticationException si las credenciales son inválidas
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) throws AuthenticationException {
        // Extrae las credenciales del body de la petición
        AuthCredentials authCredentials = new AuthCredentials();
        try {
            authCredentials = objectMapper.readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
            // Si hay error al leer las credenciales, continúa con objeto vacío
        }

        // Crea un token de autenticación con las credenciales
        // - Principal: email del usuario
        // - Credentials: contraseña
        // - Authorities: lista vacía (se llenarán después si la autenticación es exitosa)
        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );

        // Delega la autenticación al AuthenticationManager configurado
        return getAuthenticationManager().authenticate(usernamePAT);
    }

    /**
     * Se ejecuta cuando la autenticación es exitosa
     * Genera el token JWT y lo incluye en la respuesta
     * 
     * @param request Petición HTTP original
     * @param response Respuesta HTTP donde se incluirá el token
     * @param chain Cadena de filtros de seguridad
     * @param authResult Resultado de la autenticación exitosa
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        // Extraer roles del usuario autenticado
        String token = tokenUtils.createToken(
            userDetails.getName(),
            userDetails.getUsername(),
            userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        // Agregar token al header
        response.addHeader("Authorization", "Bearer " + token);
        
        // Agregar token al body como JSON
        response.setContentType("application/json");
        TokenResponse tokenResponse = new TokenResponse(token);
        objectMapper.writeValue(response.getWriter(), tokenResponse);
        response.getWriter().flush();

    }
}
