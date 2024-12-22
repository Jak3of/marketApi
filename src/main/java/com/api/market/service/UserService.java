package com.api.market.service;

import com.api.market.bean.RegisterRequest;
import com.api.market.model.Role;
import com.api.market.model.User;
import com.api.market.repository.RoleRepository;
import com.api.market.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true); // Aseguramos que el usuario esté activo por defecto

        // Asignar roles
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            request.getRoles().forEach(roleName -> {
                try {
                    Role role = roleRepository.findByName(Role.RoleName.valueOf(roleName))
                            .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleName));
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Rol inválido: " + roleName);
                }
            });
        } else {
            // Si no se especifican roles, asignar ROLE_CUSTOMER por defecto
            Role customerRole = roleRepository.findByName(Role.RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role CUSTOMER no encontrado."));
            roles.add(customerRole);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}
