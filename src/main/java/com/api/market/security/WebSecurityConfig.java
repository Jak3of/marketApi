package com.api.market.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security
 * Define cómo se maneja la autenticación y autorización en la aplicación
 * 
 * Características principales:
 * - Autenticación basada en JWT
 * - Almacenamiento de usuarios en memoria
 * - Endpoints públicos y protegidos
 * - Sin manejo de sesiones (stateless)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final TokenUtils tokenUtils;

    /**
     * Configura la cadena de filtros de seguridad
     * Define qué endpoints son públicos y cuáles requieren autenticación
     * 
     * @param http Configuración de seguridad HTTP
     * @param authManager Gestor de autenticación
     * @return Cadena de filtros configurada
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        // Configura el filtro de autenticación JWT
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(tokenUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        return http
                .csrf(csrf -> csrf.disable())  // Deshabilitamos CSRF porque usamos tokens
                .authorizeHttpRequests(auth -> {
                    // Endpoints públicos
                    auth.requestMatchers("/auth/**", "/api/auth/**", "/api/public/**").permitAll();
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/staff/**").hasAnyRole("ADMIN", "STAFF");
                    
                    // Endpoints específicos
                    auth.requestMatchers(
                        "/api/categories/**",
                        "/api/products/**"
                    ).hasAnyRole("ADMIN", "STAFF");
                    
                    // Todo lo demás requiere autenticación
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    // No usamos sesiones porque JWT es stateless
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // Agregamos los filtros JWT en el orden correcto
                .addFilter(jwtAuthenticationFilter)  // Primero autenticación
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)  // Luego autorización
                .build();
    }

    /**
     * Configura el codificador de contraseñas
     * Usa BCrypt para hashear las contraseñas de forma segura
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el gestor de autenticación
     * Lo conecta con el servicio de usuarios y el codificador de contraseñas
     */
    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
