package com.api.market.bean;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Clase que representa las credenciales de autenticación enviadas por el cliente
 * Se usa cuando el usuario intenta hacer login
 * 
 * email: correo del usuario (usado como username)
 * password: contraseña del usuario
 */
@Data
public class AuthCredentials {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
