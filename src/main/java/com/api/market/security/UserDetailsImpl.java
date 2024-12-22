package com.api.market.security;

import com.api.market.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementación de UserDetails para nuestro usuario
 * UserDetails es la interfaz que Spring Security usa para representar usuarios
 * 
 * En este caso es una implementación que:
 * - Maneja roles/autoridades (getAuthorities())
 * - Todas las cuentas están activas y no expiran
 * - Solo almacena nombre, email y contraseña
 */
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // Usamos el email como username
    }

    public String getName() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Cuenta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Cuenta nunca se bloquea
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credenciales nunca expiran
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();  // Cuenta está activa si el usuario está activo
    }
}
