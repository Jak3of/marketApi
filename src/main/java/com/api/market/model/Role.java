package com.api.market.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public enum RoleName {
        ROLE_ADMIN,     // Acceso total al sistema
        ROLE_STAFF,     // Puede gestionar productos y categorías
        ROLE_CUSTOMER   // Solo puede ver productos y hacer compras
    }
}
