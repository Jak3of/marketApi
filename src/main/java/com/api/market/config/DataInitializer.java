package com.api.market.config;

import com.api.market.model.Role;
import com.api.market.model.User;
import com.api.market.repository.RoleRepository;
import com.api.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear roles si no existen
        Arrays.stream(Role.RoleName.values()).forEach(roleName -> {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        });

        // Crear usuario admin por defecto si no existe
        if (!userRepository.existsByEmail("admin@market.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@market.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Arrays.asList(
                roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN no encontrado."))
            )));
            userRepository.save(admin);
        }
    }
}
